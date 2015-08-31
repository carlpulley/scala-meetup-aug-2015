package cakesolutions

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Main extends App with Service {

  implicit val system = ActorSystem("KebabApplication")
  implicit val materializer = ActorMaterializer()

  val config = system.settings.config
  val hostname = config.getString("hostname")
  val port = config.getInt("port")

  system.log.info("We started!")

  val bindingFuture = Http().bindAndHandle(route(config), hostname, port)

}
