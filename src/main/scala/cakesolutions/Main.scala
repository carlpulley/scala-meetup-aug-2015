package cakesolutions

import akka.actor.ActorSystem
import cakesolutions.menu.ValidatedLoad

object Main extends App with ValidatedLoad {

  implicit val system = ActorSystem("KebabApplication")

  val config = system.settings.config
  val log = system.log

  log.info("We started!")

  log.info(s"Loaded menu items: ${load(config)}")

}
