package httpservices

import java.util.UUID

import actors.RegisterMovie
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.StatusCodes.{Forbidden, InternalServerError, OK}
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess, path, post, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Inject
import common.{ActorRegistry, MovieSystem}
import models.Movie
import play.api.libs.json.{JsObject, JsString, Json}
import services.{AlreadyRegistered, RegisterError, RegisterSuccess}
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration.DurationInt

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val um: Unmarshaller[HttpEntity, JsObject] = {
    Unmarshaller.byteStringUnmarshaller.mapWithCharset { (data, charset) =>
      Json.parse(data.toArray).as[JsObject]
    }
  }
}

class MovieHttpService @Inject()(actorRegistry: ActorRegistry) extends Protocols with MovieSystem {
  private val movieActor = actorRegistry.movieActor
  private val mainActor = actorRegistry.mainActor
  implicit val timeout = Timeout(10 seconds)

  val route: Route =
    path("movies") {
      post {
        entity(as[JsObject]) { movieJson =>
          val movie = Movie(movieJson + ("_id" -> JsString(UUID.randomUUID().toString)))
          logger.info(s"Movie: ${movie.title} register requested")
          val registerResult = movieActor.ask(RegisterMovie(movie))(timeout, sender = mainActor)
          onSuccess(registerResult.map {
            case RegisterSuccess() => complete(OK, s"Movie: ${movie.title} registered")
            case AlreadyRegistered() => complete(Forbidden, s"Movie: ${movie.title} already registered")
            case RegisterError() => complete(InternalServerError, s"Movie: ${movie.title} not registered")
          }) { res => res }
        }
      }
    } ~
      path("reservations") {
        post {
          ???
        }
      }
  override val logger: LoggingAdapter = Logging(system, getClass)
}