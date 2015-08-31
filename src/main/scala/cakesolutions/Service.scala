package cakesolutions

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler, Route, ValidationRejection}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import cakesolutions.menu.ValidatedLoad
import com.typesafe.config.Config
import scala.concurrent.Future

trait Service extends ValidatedLoad {

  import Protocols._
  import StatusCodes._

  private[this] def menu(config: Config): Route =
    path("menu") {
      get {
        complete {
          ???
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
            menu(system.settings.config)
          }
        }
      }
    }

}
