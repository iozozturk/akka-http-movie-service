package actors

import akka.actor.{Actor, ActorLogging}
import models.Movie

case class RegisterMovie(movie: Movie)

trait RegisterResult
case class RegisterSuccess() extends RegisterResult
case class RegisterError() extends RegisterResult
case class Result(registerResult: RegisterResult)

class MovieActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case RegisterMovie(movie) =>
      log.info(s"Registering new movie:${movie.title}")
      sender ! RegisterSuccess
    case _ =>
      log.error("Unknown request")
      sender ! RegisterError
  }
}
