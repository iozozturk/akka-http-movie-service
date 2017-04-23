package services

import com.google.inject.Inject
import models.Movie
import repos.MovieRepo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

// @formatter:off
  trait RegisterResult
  case class RegisterSuccess() extends RegisterResult
  case class AlreadyRegistered() extends RegisterResult
  case class RegisterError() extends RegisterResult
  // @formatter:on

class MovieService @Inject()(movieRepo: MovieRepo) {

  def checkAndRegisterMovie(movie: Movie): Future[RegisterResult] = {
    movieRepo.findMovieByImdbId(movie.imdbId).map { movie =>
      AlreadyRegistered()
    }.fallbackTo {
      movieRepo.createMovie(movie).map(_ => RegisterSuccess())
    }
  }

}
