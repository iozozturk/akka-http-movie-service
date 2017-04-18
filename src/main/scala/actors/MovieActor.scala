package actors

import akka.actor.{Actor, ActorLogging}
import models.Movie
import mongo.MongoCollectionFactory
import play.api.libs.json.{JsObject, Json}

case class RegisterMovie(movie: Movie)

trait RegisterResult
case class RegisterSuccess() extends RegisterResult
case class RegisterError() extends RegisterResult

class MovieActor extends Actor with ActorLogging {
  private val coll = new MongoCollectionFactory().makeCollection("movies")
  implicit val movieFormats = Json.format[Movie]

  override def receive: Receive = {
    case RegisterMovie(movie) =>
      log.info(s"Registering new movie:${movie.title}")
      coll.insert(Json.toJson(movie).as[JsObject])
      sender ! RegisterSuccess
    case _ =>
      log.error("Unknown request")
      sender ! RegisterError
  }
}
