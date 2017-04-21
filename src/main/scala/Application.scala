import akka.event.Logging
import akka.http.scaladsl.Http
import services.MovieService

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

object Application extends MovieService {
  val logger = Logging(system, getClass)

  def main(args: Array[String]): Unit = {

    val binding = Await.result(Http().bindAndHandle(route, "0.0.0.0", 9000), 3.seconds)

    println(s"Started server at ${binding.localAddress}")
  }

}
