package services

import com.google.inject.Inject
import models.{Movie, Reservation}
import repos.MovieRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// @formatter:off
sealed trait RegisterResult
case class RegisterSuccess() extends RegisterResult
case class AlreadyRegistered() extends RegisterResult
case class RegisterError() extends RegisterResult

sealed trait ReservationResult
case class ReservationError(cause:String) extends ReservationResult
case class ReservationSuccess() extends ReservationResult

sealed trait ReservationInfo
case class ReservationInfoSuccess(movie: Movie) extends ReservationInfo
case class ReservationInfoError(cause:String)extends ReservationInfo
// @formatter:on

class MovieService @Inject()(movieRepo: MovieRepo) {

  def getMovie(reservation: Reservation): Future[ReservationInfo] = {
    movieRepo.findMovieByImdbId(reservation.imdbId).map { movie =>
      ReservationInfoSuccess(movie)
    }.fallbackTo {
      Future {
        ReservationInfoError("Could not find the movie")
      }
    }
  }

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
        movieRepo.setSeats(movie, movie.availableSeats - 1, movie.reservedSeats + 1).map { _ =>
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
