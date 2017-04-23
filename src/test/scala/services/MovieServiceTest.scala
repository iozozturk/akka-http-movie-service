package services

import models.Movie
import org.mockito.Mockito.when
import org.mongodb.scala.Completed
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import repos.MovieRepo
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class MovieServiceTest extends WordSpec with Matchers with MockitoSugar {

  val movieRepo = mock[MovieRepo]
  val movie = Movie(Json.obj("imdbId" -> "tt01"))
  val movieService = new MovieService(movieRepo)


  "reply with RegisterSuccess message when movie does not exist" in {
    when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future.failed(new Exception())
    when(movieRepo.createMovie(movie)) thenReturn Future {
      new Completed
    }

    val result = movieService.checkAndRegisterMovie(movie)
    result.map {
      case RegisterSuccess() => assert(true)
      case m => assert(false, s"Actor replied with unexpected message:$m")
    }
  }

  "reply with AlreadyRegistered message when movie registered before" in {
    when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future {
      movie
    }
    val result = movieService.checkAndRegisterMovie(movie)
    result.map {
      case AlreadyRegistered() => assert(true)
      case m => assert(false, s"Actor replied with unexpected message:$m")
    }
  }

}
