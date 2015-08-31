package cakesolutions

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import cakesolutions.menu.{Item, Order}
import spray.json.DefaultJsonProtocol

object Protocols extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val itemFormat = jsonFormat2(Item)
  implicit val orderFormat = jsonFormat1(Order)

}
