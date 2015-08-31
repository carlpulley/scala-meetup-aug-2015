package cakesolutions.menu

import com.typesafe.config.Config

import scala.util.{Success, Failure, Try}

class ValidatedLoad {

  private[this] def loadItem(item: Config): Try[Item] = {
    if (! item.hasPath("name") || ! item.hasPath("price")) {
      return Failure(new Exception(s"Misconfiguration detected - menu item $item is missing a `name` or a `price`"))
    }

    val description = item.getString("name")
    val price = item.getDouble("price")

    Try(Item(description, price))
  }

  /**
   * Validates the loaded configuration object to return a validated menu.
   *
   * @param config configuration object that `Order` will load and validate menu items from
   * @return validated list of menu items
   */
  def load(config: Config): Try[Order] = {
    if (! config.hasPath("menu.items")) {
      return Failure(new Exception("Misconfiguration detected - could not locate path menu.items"))
    }

    val items = config.getConfigList("menu.items")

    val validatedItems = for (item <- items) yield loadItem(item)

    Success(Order(validatedItems))
  }

}
