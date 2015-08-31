package cakesolutions

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._

object Main extends App with Service {

  implicit val system = ActorSystem("KebabApplication")
  implicit val materializer = ActorMaterializer()

  val config = system.settings.config
  val hostname = config.getString("hostname")
  val port = config.getInt("port")
  val log = system.log

  implicit val timeout = Timeout(config.getDuration("timeout", TimeUnit.SECONDS).seconds)

  log.info("We started!")

  val bindingFuture = Http().bindAndHandle(route, hostname, port)

}
