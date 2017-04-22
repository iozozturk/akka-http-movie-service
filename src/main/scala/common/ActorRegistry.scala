package common

import javax.inject.Inject

import actors.{MainActor, MovieActor}
import akka.actor.{ActorRef, Props}
import akka.event.Logging
import com.google.inject.Singleton
import repos.MovieRepo

@Singleton
class ActorRegistry @Inject()(movieRepo: MovieRepo) extends MovieSystem {
  val logger = Logging(system, getClass)
  logger.info("ACTORS UP")
  val movieActor: ActorRef = system.actorOf(Props(new MovieActor(movieRepo)), "movieActor")
  val mainActor = system.actorOf(Props[MainActor], "mainActor")
}
