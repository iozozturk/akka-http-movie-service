package actors

import akka.actor.{Actor, ActorLogging}
import models.Movie
import repos.MovieRepo

case class RegisterMovie(movie: Movie)

trait RegisterResult
case class RegisterSuccess() extends RegisterResult
case class RegisterError() extends RegisterResult

class MovieActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case RegisterMovie(movie) =>
      log.info(s"Registering new movie:${movie.title}")
      MovieRepo.createMovie(movie)
      sender ! RegisterSuccess
    case _ =>
      log.error("Unknown request")
      sender ! RegisterError
  }
}
