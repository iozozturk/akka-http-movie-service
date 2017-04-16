package actors

import akka.actor.{Actor, ActorLogging}
import models.Movie

case class RegisterMovie(movie: Movie)

class MovieActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case RegisterMovie(movie) =>
      log.info(s"Registering new movie:${movie.title}")
    case _ =>
      log.error("Unknown request")
  }
}
