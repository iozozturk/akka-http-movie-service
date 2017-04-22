package services

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import common.ActorRegistry
import models.Movie
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.mongodb.scala.Completed
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.json.Json
import repos.MovieRepo

import scala.concurrent.Future

class MovieServiceTest extends AsyncWordSpec
  with Matchers with ScalatestRouteTest with MockitoSugar {

  private val repo = mock[MovieRepo]
  val movieService = new MovieService(new ActorRegistry(repo))

  "Movie Service" should {
    val movie = Movie(Json.obj("imdbId" -> "tt01", "title" -> "shawshank"))

    "register a new movie successfully" in {
      when(repo.findMovieByImdbId(any[String])) thenReturn
        Future.failed(new Exception("movie does not exist"))
      when(repo.createMovie(any[Movie])) thenReturn Future {
        Completed()
      }
      Post("/movies", movie.json.toString()) ~> movieService.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "Movie: shawshank registered"
      }
    }

    "reply with already registered message when movie is already registered" in {
      when(repo.findMovieByImdbId(any[String])) thenReturn
        Future {
          movie
        }
      Post("/movies", movie.json.toString()) ~> movieService.route ~> check {
        status shouldEqual StatusCodes.Forbidden
        responseAs[String] shouldEqual "Movie: shawshank already registered"
      }
    }

  }
}
