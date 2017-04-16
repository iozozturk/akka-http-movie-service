import actors._
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import common.{ActorRegistry, MovieSystem}
import models.Movie
import spray.json.DefaultJsonProtocol

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong


trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val movieFormat = jsonFormat4(Movie)
}

trait MovieService extends Protocols with MovieSystem {
  private val movieActor = ActorRegistry.movieActor
  implicit val timeout = Timeout(10 seconds)

  val route =
    path("movies") {
      post {
        entity(as[Movie]) { movie =>
          logger.info(s"Movie: ${movie.title} register requested")
          val registerResult = movieActor ? RegisterMovie(movie)
          onSuccess(registerResult.map {
            case RegisterSuccess => complete(s"Movie: ${movie.title} registered")
            case RegisterError => complete(s"Movie: ${movie.title} not registered")
          }) { res => res }
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

  def main(args: Array[String]): Unit = {

    val binding = Await.result(Http().bindAndHandle(route, "0.0.0.0", 9000), 3.seconds)

    println("Started server at 0.0.0.0:9000")
  }

}
