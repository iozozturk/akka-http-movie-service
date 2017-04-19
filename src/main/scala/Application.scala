import java.util.UUID

import actors._
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.pattern.ask
import akka.util.Timeout
import common.{ActorRegistry, MovieSystem}
import models.Movie
import play.api.libs.json.{JsObject, JsString, Json}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

protected trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val um: Unmarshaller[HttpEntity, JsObject] = {
    Unmarshaller.byteStringUnmarshaller.mapWithCharset { (data, charset) =>
      Json.parse(data.toArray).as[JsObject]
    }
  }
}

protected trait MovieService extends Protocols with MovieSystem {
  private val movieActor = ActorRegistry.movieActor
  implicit val timeout = Timeout(10 seconds)

  val route: Route =
    path("movies") {
      post {
        entity(as[JsObject]) { movieJson =>
          val movie = Movie(movieJson + ("_id" -> JsString(UUID.randomUUID().toString)))
          logger.info(s"Movie: ${movie.title} register requested")
          val registerResult = movieActor ? RegisterMovie(movie)
          onSuccess(registerResult.map {
            case RegisterSuccess => complete(OK, s"Movie: ${movie.title} registered")
            case RegisterError => complete(InternalServerError, s"Movie: ${movie.title} not registered")
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

    println(s"Started server at ${binding.localAddress}")
  }

}
