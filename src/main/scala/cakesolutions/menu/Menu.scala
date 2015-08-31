package cakesolutions.menu

case class Item(name: String, price: Double, tag: Option[String] = None) {
  require(name.nonEmpty)
  require(price > 0)
}

case class OrderItem(name: String)

case class Order(items: Vector[Item])
