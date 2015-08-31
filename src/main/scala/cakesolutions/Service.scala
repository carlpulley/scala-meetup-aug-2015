package cakesolutions

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler, Route, ValidationRejection}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import cakesolutions.actor.Campaign
import cakesolutions.menu.{Order, OrderItem, ValidatedLoad}
import com.typesafe.config.Config
import java.util.UUID
import scala.concurrent.Future

trait Service extends ValidatedLoad {

  import Campaign._
  import Protocols._
  import StatusCodes._

  // TODO: deal with actor resource leaks!
  private[this] var processor = Map.empty[UUID, ActorRef]

  private[this] def menu(config: Config): Route =
    path("menu") {
      get {
        complete {
          Future.fromTry(load(config))
        }
      }
    }

  private[this] def campaign(implicit system: ActorSystem, timeout: Timeout): Route =
    pathPrefix("campaign") {
      pathPrefix(JavaUUID) { id =>
        validate(processor.contains(id), s"$id is an invalid campaign identifier") {
          pathEnd {
            get {
              complete {
                (processor(id) ? ViewCampaign).mapTo[Order]
              }
            }
          } ~
          path("complete") {
            post {
              complete {
                system.stop(processor(id))
                processor -= id

                "Please pick up your order"
              }
            }
          }
        }
      } ~
      pathEnd {
        post {
          complete {
            val id = UUID.randomUUID()
            processor += id -> system.actorOf(Props[Campaign])

            id.toString
          }
        }
      }
    }

  private[this] def order(implicit materializer: ActorMaterializer, timeout: Timeout): Route =
    pathPrefix("order" / JavaUUID / Segment) { (id, tag) =>
      validate(processor.contains(id), s"$id is an invalid campaign identifier") {
        pathEnd {
          get {
            complete {
              (processor(id) ? ViewOrder(tag)).mapTo[Order]
            }
          }
        } ~
        path("delete") {
          post {
            entity(as[OrderItem]) { order =>
              processor(id) ! DeleteFromOrder(order, tag)

              complete(s"Removed $order from existing order")
            }
          }
        } ~
        pathEnd {
          post {
            entity(as[OrderItem]) { order =>
              complete {
                (processor(id) ? AddToOrder(order, tag)).mapTo[String]
              }
            }
          }
        }
      }
    }

  private[this] def exceptionHandler(log: LoggingAdapter) = ExceptionHandler {
    case exn =>
      extractUri { uri =>
        log.error(s"Request to $uri threw the exception $exn")

        complete(HttpResponse(InternalServerError, entity = exn.getMessage))
      }
  }

  private[this] def rejectionHandler(log: LoggingAdapter) =
    RejectionHandler
      .newBuilder()
      .handle { case ValidationRejection(msg, cause) =>
        log.error(s"$msg - reason: $cause")

        complete(Unauthorized, msg)
      }
      .result()

  def route(implicit system: ActorSystem, materializer: ActorMaterializer, timeout: Timeout): Route =
    logRequest("HTTPRequest") {
      logResult("HTTPResponse") {
        handleExceptions(exceptionHandler(system.log)) {
          handleRejections(rejectionHandler(system.log)) {
            menu(system.settings.config) ~ campaign ~ order
          }
        }
      }
    }

}
