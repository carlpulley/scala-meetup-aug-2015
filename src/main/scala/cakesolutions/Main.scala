package cakesolutions

import akka.actor.ActorSystem

object Main extends App {

  implicit val system = ActorSystem("KebabApplication")

  system.log.info("We started!")

}
