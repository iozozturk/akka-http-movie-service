package common

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

trait MovieSystem {
  implicit val system: ActorSystem = ActorSystem("movie-system")
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val logger: LoggingAdapter

}