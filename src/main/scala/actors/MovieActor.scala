package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import models.Movie
import repos.MovieRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// @formatter:off
case class RegisterMovie(movie: Movie)

trait RegisterResult
case class RegisterSuccess() extends RegisterResult
case class AlreadyRegistered() extends RegisterResult
case class RegisterError() extends RegisterResult
// @formatter:on

class MovieActor(movieRepo: MovieRepo) extends Actor with ActorLogging {

  override def receive: Receive = {
    case RegisterMovie(movie) =>
      log.info(s"Registering new movie:$movie")
      checkAndRegisterMovie(movie) pipeTo sender()
    case _ =>
      log.error("Unknown request")
      sender ! RegisterError
  }

  private def checkAndRegisterMovie(movie: Movie): Future[RegisterResult] = {
    movieRepo.findMovieByImdbId(movie.imdbId).map { movie =>
      AlreadyRegistered()
    }.fallbackTo {
      movieRepo.createMovie(movie).map(_ => RegisterSuccess())
    }
  }
}

class MainActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case _ =>
      log.error("Unknown request")
  }

}
