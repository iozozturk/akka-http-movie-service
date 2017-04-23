package common

import javax.inject.Inject

import actors.{MainActor, MovieActor}
import akka.actor.{ActorRef, Props}
import akka.event.Logging
import com.google.inject.Singleton
import services.MovieService

@Singleton
class ActorRegistry @Inject()(movieService: MovieService) extends MovieSystem {
  val logger = Logging(system, getClass)
  logger.info("ACTORS UP")
  val movieActor: ActorRef = system.actorOf(Props(new MovieActor(movieService)), "movieActor")
  val mainActor = system.actorOf(Props[MainActor], "mainActor")
}
