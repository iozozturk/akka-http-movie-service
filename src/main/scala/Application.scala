import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import common.{ActorRegistry, MovieSystem}
import models.Movie
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, ExecutionContextExecutor}


trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val movieFormat = jsonFormat4(Movie)
}

trait MovieService extends Protocols with MovieSystem {

  val route =
    path("movies") {
      post {
        entity(as[Movie]) { movie =>
          logger.info(s"Movie: ${movie.title} register requested")
          complete(s"Movie: ${movie.title} registered")
        }
      }
    } ~
      path("reservations") {
        post {
          ???
        }
      }
}

object Application extends MovieService {
  val logger = Logging(system, getClass)
  ActorRegistry.movieActor

  def main(args: Array[String]): Unit = {

    val binding = Await.result(Http().bindAndHandle(route, "0.0.0.0", 9000), 3.seconds)

    println("Started server at 0.0.0.0:9000")
  }

}
