import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import models.Movie
import spray.json.DefaultJsonProtocol

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration.DurationLong


trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val movieFormat = jsonFormat4(Movie)
}

trait MovieService extends Protocols {
  implicit val system: ActorSystem
  implicit val executor: ExecutionContextExecutor
  implicit val materializer: Materializer
  val logger: LoggingAdapter

  val route =
    path("movies") {
      post {
        entity(as[Movie]) { movie =>
          logger.info(s"Registering movie: ${movie.title}")
          complete(s"Movie: ${movie.title} registered")
        }
      }
    } ~
      path("reservations") {
        post {
          ???
        }
      }
}

object Application extends MovieService {
  override implicit val system: ActorSystem = ActorSystem()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val logger = Logging(system, getClass)

  def main(args: Array[String]): Unit = {

    val binding = Await.result(Http().bindAndHandle(route, "0.0.0.0", 9000), 3.seconds)

    println("Started server at 0.0.0.0:9000")
  }

}
