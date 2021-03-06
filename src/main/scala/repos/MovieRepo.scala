package repos

import com.google.inject.Singleton
import models.Movie
import mongo.MongoCollectionFactory
import org.mongodb.scala.Completed
import play.api.libs.json.{JsString, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MovieRepo {

  private val coll = new MongoCollectionFactory().makeCollection("movies")
  implicit val movieFormats = Json.format[Movie]

  def createMovie(movie: Movie): Future[Completed] = {
    coll.insert(movie.json)
  }

  def findMovie(_id: String): Future[Movie] = {
    coll.findById(_id).map(Movie)
  }

  def findMovieByImdbId(imdbId: String): Future[Movie] = {
    coll.findOne(Json.obj("imdbId" -> JsString(imdbId))).map(m => Movie(m))
  }

  def setSeats(movie: Movie, availableSeatCount: Int, reservedSeatCount: Int) = coll.findAndUpdate(
    Json.obj("imdbId" -> movie.imdbId),
    Json.obj("availableSeats" -> availableSeatCount, "reservedSeats" -> reservedSeatCount)
  )

}
