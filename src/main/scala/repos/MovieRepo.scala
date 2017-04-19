package repos

import models.Movie
import mongo.MongoCollectionFactory
import org.mongodb.scala.Completed
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MovieRepo {
  private val coll = new MongoCollectionFactory().makeCollection("movies")
  implicit val movieFormats = Json.format[Movie]

  def createMovie(movie: Movie): Future[Completed] = {
    coll.insert(movie.json)
  }

  def findMovie(_id: String): Future[Movie] = {
    coll.findById(_id).map(Movie)
  }
}
