package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import models.Movie
import repos.MovieRepo

import scala.concurrent.ExecutionContext.Implicits.global

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
      movieRepo.createMovie(movie).map { _ =>
        RegisterSuccess
      }.pipeTo(sender())
    case _ =>
      log.error("Unknown request")
      sender ! RegisterError
  }

  //  private def checkAndRegisterMovie(movie: Movie): RegisterResult = {
  //    MovieRepo.findMovieByImdbId(movie.imdbId).map { movie =>
  //
  //    }
  //  }
}
