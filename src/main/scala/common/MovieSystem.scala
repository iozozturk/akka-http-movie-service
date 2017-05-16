package common

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

trait MovieSystem {
  implicit val system: ActorSystem = Singletons.system
  implicit val executor: ExecutionContextExecutor = Singletons.dispatcher
  implicit val materializer: ActorMaterializer = Singletons.materializer
  val logger: LoggingAdapter
}

object Singletons {
  implicit val system = ActorSystem("movie-system")
  implicit val dispatcher = system.dispatcher
  implicit val materializer = ActorMaterializer()
}