package repos

import models.Movie
import mongo.MongoCollectionFactory
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MovieRepo {
  private val coll = new MongoCollectionFactory().makeCollection("movies")
  implicit val movieFormats = Json.format[Movie]

  def createMovie(movie: Movie) = {
    coll.insert(Json.toJson(movie).as[JsObject])
  }

  def findMovie(_id: String): Future[Movie] = {
    coll.findById(_id).map(Movie(_))
  }
}
