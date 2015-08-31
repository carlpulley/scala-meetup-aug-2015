package cakesolutions

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cakesolutions.menu.ValidatedLoad
import com.typesafe.config.Config
import scala.concurrent.Future

trait Service extends ValidatedLoad {

  import Protocols._

  def route(config: Config): Route =
    pathPrefix("menu") {
      get {
        complete {
          Future.fromTry(load(config))
        }
      }
    }

}
