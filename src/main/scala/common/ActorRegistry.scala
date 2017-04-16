package common

import actors.MovieActor
import akka.actor.{ActorRef, Props}
import akka.event.Logging

object ActorRegistry extends MovieSystem {
  val logger = Logging(system, getClass)
  logger.info("ACTORS UP")
  val movieActor: ActorRef = system.actorOf(Props(new MovieActor), "movieActor")
}
