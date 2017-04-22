package repos

import common.FreshDbContext
import models.Movie
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.Await
import scala.concurrent.duration.{DurationLong, FiniteDuration}

class MovieRepoTest extends AsyncWordSpec with Matchers with FreshDbContext {
  val atMost: FiniteDuration = 10.seconds

  "Movie Repo" should {
    val movie = Movie(Json.parse(
      """
        |{
        | "_id": "id",
        | "imdbId": "tt01",
        | "title": "title",
        | "availableSeats": 1,
        | "screenId": "screenId"
        |}
      """.stripMargin).as[JsObject])

    val movieRepo = new MovieRepo

    "insert movie" in {
      Await.ready(movieRepo.createMovie(movie), atMost).map(s => assert(true))
    }

    "find movie" in {
      Await.ready(movieRepo.createMovie(movie), atMost)
      Await.ready(movieRepo.findMovie("id"), atMost).map { movie =>
        movie.imdbId shouldEqual "tt01"
      }
    }

    "find movie by ImdbId" in {
      Await.ready(movieRepo.createMovie(movie), atMost)
      Await.ready(movieRepo.findMovieByImdbId("tt01"), atMost).map { movie =>
        movie.imdbId shouldEqual "tt01"
      }
    }
  }
}
