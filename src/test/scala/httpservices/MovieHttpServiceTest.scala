package httpservices

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import common.ActorRegistry
import models.{Movie, Reservation}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.json.Json
import services.{AlreadyRegistered, MovieService, RegisterSuccess, ReservationSuccess}

import scala.concurrent.Future

class MovieHttpServiceTest extends AsyncWordSpec
  with Matchers with ScalatestRouteTest with MockitoSugar {

  private val movieService = mock[MovieService]
  val movieHttpService = new MovieHttpService(new ActorRegistry(movieService))

  "Movie Service" should {
    val movie = Movie(Json.obj("imdbId" -> "tt01", "title" -> "shawshank"))
    val reservation = Reservation(Json.obj("imdbId" -> "tt01", "screenId" -> "scrId"))

    "register a new movie successfully" in {
      when(movieService.checkAndRegisterMovie(any[Movie])) thenReturn
        Future {
          RegisterSuccess()
        }
      Post("/movies", movie.json.toString()) ~> movieHttpService.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "Movie: shawshank registered"
      }
    }

    "reply with already registered message when movie is already registered" in {
      when(movieService.checkAndRegisterMovie(any[Movie])) thenReturn
        Future {
          AlreadyRegistered()
        }
      Post("/movies", movie.json.toString()) ~> movieHttpService.route ~> check {
        status shouldEqual StatusCodes.Forbidden
        responseAs[String] shouldEqual "Movie: shawshank already registered"
      }
    }


    "reserve a movie successfully" in {
      when(movieService.reserve(any[Reservation])) thenReturn
        Future {
          ReservationSuccess()
        }
      Post("/reservations", reservation.json.toString()) ~> movieHttpService.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "Movie reserved: Reservation({\"imdbId\":\"tt01\",\"screenId\":\"scrId\"})"
      }
    }

  }
}
