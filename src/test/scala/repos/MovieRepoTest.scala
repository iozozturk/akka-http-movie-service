package repos

import models.Movie
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.{DurationLong, FiniteDuration}

class MovieRepoTest extends AsyncWordSpec with Matchers  {
  val atMost: FiniteDuration = 10.seconds

  "MovieRepo" should {
    "insert movie to db" in {
      Await.ready(MovieRepo.createMovie(Movie("id", "title", 1, "screenId")), atMost).map(s => assert(true))
    }
    "find movie from db" in {
      Await.ready(MovieRepo.createMovie(Movie("id", "title", 1, "screenId")), atMost)
      Await.ready(MovieRepo.findMovie("id"), atMost).map { movie =>
        movie.imdbId shouldEqual "id"
      }
    }
  }
}
