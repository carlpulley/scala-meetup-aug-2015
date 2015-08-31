package cakesolutions.actor

import cakesolutions.LoggingActor
import cakesolutions.menu.{Item, ValidatedLoad}
import scala.language.postfixOps

object Campaign {

  type Label = String

  sealed trait CampaignMessage

  case object ViewCampaign extends CampaignMessage

}

class Campaign extends LoggingActor with ValidatedLoad {

  import Campaign._

  val menu = load(context.system.settings.config).get

  var orders = Vector.empty[Item]

  def receive: Receive = {
    case ViewCampaign =>
      ???
  }

}
