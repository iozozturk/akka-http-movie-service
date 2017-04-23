package services

import models.{Movie, Reservation}
import org.mockito.Mockito.when
import org.mongodb.scala.Completed
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.json.Json
import repos.MovieRepo

import scala.concurrent.Future

class MovieServiceTest extends AsyncWordSpec with Matchers with MockitoSugar {

  val movieRepo = mock[MovieRepo]
  val movie = Movie(Json.obj("imdbId" -> "tt01", "availableSeats" -> 1))
  val reservation = Reservation(Json.obj("imdbId" -> "tt01", "screenId" -> "screenId"))
  val movieService = new MovieService(movieRepo)


  "reply with RegisterSuccess message when movie does not exist" in {
    when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future.failed(new Exception())
    when(movieRepo.createMovie(movie)) thenReturn Future {
      new Completed
    }

    val result = movieService.checkAndRegisterMovie(movie)
    result.map {
      case RegisterSuccess() => assert(true)
      case m => assert(false, s"Service replied with unexpected message:$m")
    }
  }

  "reply with AlreadyRegistered message when movie registered before" in {
    when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future {
      movie
    }
    val result = movieService.checkAndRegisterMovie(movie)
    result.map {
      case AlreadyRegistered() => assert(true)
      case m => assert(false, s"Service replied with unexpected message:$m")
    }
  }

  "reserve a movie successfully" in {
    when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future {
      movie
    }
    when(movieRepo.setAvailableSeats(movie, 0)) thenReturn Future {
      movie.json
    }
    val result = movieService.reserve(reservation)
    result.map {
      case ReservationSuccess() => assert(true)
      case m => assert(false, s"Service replied with unexpected message:$m")
    }
  }

  "try to reserve a movie that has not enough room left" in {
    val movie = Movie(Json.obj("imdbId" -> "tt01", "availableSeats" -> 0))
    when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future {
      movie
    }
    val result = movieService.reserve(reservation)
    result.map {
      case ReservationError(message) => message shouldEqual "Insufficient room for this movie"
      case m => assert(false, s"Service replied with unexpected message:$m")
    }
  }

}
