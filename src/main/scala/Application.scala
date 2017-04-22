import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import com.google.inject.Guice
import common.{ActorRegistry, MovieSystem}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import net.codingwell.scalaguice.ScalaModule
import repos.MovieRepo
import services.MovieService

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

object Application extends MovieSystem {

  def main(args: Array[String]): Unit = {

    val injector = Guice.createInjector()

    val movieService = injector.instance[MovieService]
    val route = movieService.route

    val binding = Await.result(Http().bindAndHandle(route, "0.0.0.0", 9000), 3.seconds)

    println(s"Started server at ${binding.localAddress}")
  }

  override val logger: LoggingAdapter = Logging(system, getClass)
}

object MainModule extends ScalaModule {

  override def configure(): Unit = {

    val movieRepo = new MovieRepo
    bind[ActorRegistry].toInstance(new ActorRegistry(movieRepo))

  }
}
