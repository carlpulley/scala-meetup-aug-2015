package cakesolutions.menu

case class Item(description: String, price: Double) {
  require(description.nonEmpty)
  require(price > 0)
}

case class Order(items: Vector[Item])
