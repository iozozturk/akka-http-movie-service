package repos

import common.FreshDbContext
import models.Movie
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.Await
import scala.concurrent.duration.{DurationLong, FiniteDuration}

class MovieRepoTest extends AsyncWordSpec with Matchers with FreshDbContext {
  val atMost: FiniteDuration = 10.seconds

  "MovieRepo" should {
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

    "insert movie" in {
      Await.ready(MovieRepo.createMovie(movie), atMost).map(s => assert(true))
    }

    "find movie" in {
      Await.ready(MovieRepo.createMovie(movie), atMost)
      Await.ready(MovieRepo.findMovie("id"), atMost).map { movie =>
        movie.imdbId shouldEqual "tt01"
      }
    }

    "find movie by ImdbId" in {
      Await.ready(MovieRepo.createMovie(movie), atMost)
      Await.ready(MovieRepo.findMovieByImdbId("tt01"), atMost).map { movie =>
        movie.imdbId shouldEqual "tt01"
      }
    }
  }
}
