package cakesolutions.menu

case class Item(name: String, price: Double) {
  require(name.nonEmpty)
  require(price > 0)
}

case class Order(items: Vector[Item])
