package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import models.{Movie, Reservation}
import services.{MovieService, RegisterError}

import scala.concurrent.ExecutionContext.Implicits.global

case class RegisterMovie(movie: Movie)
case class ReserveMovie(reservation:Reservation)
case class CheckMovie(reservation:Reservation)

class MovieActor(movieService: MovieService) extends Actor with ActorLogging {

  override def receive: Receive = {
    case RegisterMovie(movie) =>
      log.info(s"Registering new movie:$movie")
      movieService.checkAndRegisterMovie(movie) pipeTo sender()
    case ReserveMovie(reservation) =>
      log.info(s"Reserving movie:$reservation")
      movieService.reserve(reservation) pipeTo sender()
    case CheckMovie(reservation) =>
      log.info(s"Checking movie:$reservation")
      movieService.getMovie(reservation) pipeTo sender()
    case _ =>
      log.error("Unknown request")
      sender ! RegisterError
  }

}

class MainActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case _ =>
      log.error("Unknown request")
  }

}
