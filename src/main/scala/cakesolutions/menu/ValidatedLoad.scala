package cakesolutions.menu

import com.typesafe.config.Config
import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

trait ValidatedLoad {

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
      return Failure(new Exception("Misconfiguration detected - could not locate path `menu.items`"))
    }

    val items = config.getConfigList("menu.items")

    items.foldRight[Try[Order]](Success(Order(Vector.empty))) {
      case (_, err: Failure[Order]) =>
        err

      case (item, Success(Order(order))) =>
        for (newItem <- loadItem(item)) yield Order(newItem +: order)
    }
  }

}
