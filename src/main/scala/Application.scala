import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

object Application { //extends UlakSystem {

  def main(args: Array[String]): Unit = {
    val route =
      path("movies") {
        post {
          ???
        }
        get {
          ???
        }
      } ~
        path("reservations") {
          post {
            ???
          }
        }

//    val binding = Await.result(Http().bindAndHandle(route, "0.0.0.0", 9000), 3.seconds)

    println("Started server at 0.0.0.0:9000")
  }
}
