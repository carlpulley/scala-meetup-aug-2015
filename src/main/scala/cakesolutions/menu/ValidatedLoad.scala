package cakesolutions.menu

import com.typesafe.config.Config
import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

trait ValidatedLoad {

  /**
   * Validates the loaded configuration object to return a validated menu.
   *
   * @param config configuration object that `Order` will load and validate menu items from
   * @return validated list of menu items
   */
  def load(config: Config): Try[Order] = ???

}
