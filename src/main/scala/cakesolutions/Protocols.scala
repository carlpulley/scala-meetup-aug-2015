package cakesolutions

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import cakesolutions.menu.{Item, Order, OrderItem}
import spray.json.DefaultJsonProtocol

object Protocols extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val itemFormat = jsonFormat3(Item)
  implicit val orderItemFormat = jsonFormat1(OrderItem)
  implicit val orderFormat = jsonFormat1(Order)

}
