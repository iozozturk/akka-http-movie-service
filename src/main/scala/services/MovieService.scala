package services

import com.google.inject.Inject
import models.{Movie, Reservation}
import repos.MovieRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// @formatter:off
trait RegisterResult
case class RegisterSuccess() extends RegisterResult
case class AlreadyRegistered() extends RegisterResult
case class RegisterError() extends RegisterResult

trait ReservationResult
case class ReservationError(cause:String) extends ReservationResult
case class ReservationSuccess() extends ReservationResult
// @formatter:on

class MovieService @Inject()(movieRepo: MovieRepo) {

  def checkAndRegisterMovie(movie: Movie): Future[RegisterResult] = {
    movieRepo.findMovieByImdbId(movie.imdbId).map { movie =>
      AlreadyRegistered()
    }.fallbackTo {
      movieRepo.createMovie(movie).map(_ => RegisterSuccess())
    }
  }

  def reserve(reservation: Reservation): Future[ReservationResult] = {
    movieRepo.findMovieByImdbId(reservation.imdbId).map { movie =>
      if (movie.availableSeats > 0) {
        movieRepo.setAvailableSeats(movie, movie.availableSeats - 1).map { _ =>
          ReservationSuccess()
        }
      } else {
        Future {
          ReservationError("Insufficient room for this movie")
        }
      }
    }.flatten.fallbackTo {
      Future {
        ReservationError(s"Movie with imdbId:${reservation.imdbId} does not exist")
      }
    }
  }

}
