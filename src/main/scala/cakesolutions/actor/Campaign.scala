package cakesolutions.actor

import cakesolutions.LoggingActor
import cakesolutions.menu.{Item, Order, OrderItem, ValidatedLoad}
import scala.language.postfixOps

object Campaign {

  type Label = String

  sealed trait CampaignMessage

  case object ViewCampaign extends CampaignMessage

  case class ViewOrder(tag: Label) extends CampaignMessage
  case class AddToOrder(order: OrderItem, tag: Label) extends CampaignMessage
  case class DeleteFromOrder(order: OrderItem, tag: Label) extends CampaignMessage

}

class Campaign extends LoggingActor with ValidatedLoad {

  import Campaign._

  val menu = load(context.system.settings.config).get

  var orders = Vector.empty[Item]

  def receive: Receive = {
    case ViewCampaign =>
      sender() ! Order(orders)

    case ViewOrder(tag) =>
      sender() ! Order(orders.filter(_.tag.exists(tag ==)))

    case AddToOrder(order @ OrderItem(name), tag) =>
      val price = menu.items.find(_.name == name).map(_.price)

      if (price.isDefined) {
        price.foreach(cost => orders = Item(name, cost, Some(tag)) +: orders)

        sender() ! s"Added $order to order for $tag"
      } else {
        sender() ! s"Failed to add $order - no menu item is named $name"
      }

    case DeleteFromOrder(OrderItem(name), tag) =>
      orders = orders.filterNot(order => order.name == name && order.tag.exists(tag ==))
  }

}
