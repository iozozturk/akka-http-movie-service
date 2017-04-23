package actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit}
import models.{Movie, Reservation}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpecLike, Matchers}
import play.api.libs.json.Json
import services._

import scala.concurrent.Future

class MovieActorTest extends TestKit(ActorSystem("TestKitUsageSpec"))
  with DefaultTimeout with ImplicitSender
  with AsyncWordSpecLike with Matchers with MockitoSugar {

  val movieService = mock[MovieService]
  val movie = Movie(Json.obj("imdbId" -> "tt01"))
  val reservation = Reservation(Json.obj("imdbId" -> "tt01"))

  val movieActorRef = TestActorRef(new MovieActor(movieService))
  val movieActor = movieActorRef.underlyingActor

  "Movie Actor" should {

    "reply with RegisterSuccess message when movie does not exist" in {
      when(movieService.checkAndRegisterMovie(movie)) thenReturn Future {
        RegisterSuccess()
      }

      val reply = movieActorRef ? RegisterMovie(movie)
      reply.map {
        case RegisterSuccess() => assert(true)
        case m => assert(false, s"Actor replied with unexpected message:$m")
      }
    }

    "reply with AlreadyRegistered message when movie registered before" in {
      when(movieService.checkAndRegisterMovie(movie)) thenReturn Future {
        AlreadyRegistered()
      }
      val reply = movieActorRef ? RegisterMovie(movie)
      reply.map {
        case AlreadyRegistered() => assert(true)
        case m => assert(false, s"Actor replied with unexpected message:$m")
      }
    }

    "reply with ReservationSuccess message when movie can be reserved" in {
      when(movieService.reserve(reservation)) thenReturn Future {
        ReservationSuccess()
      }
      val reply = movieActorRef ? ReserveMovie(reservation)
      reply.map {
        case ReservationSuccess() => assert(true)
        case m => assert(false, s"Actor replied with unexpected message:$m")
      }
    }

    "reply with ReservationInfoSuccess message when movie can be found" in {
      when(movieService.getMovie(reservation)) thenReturn Future {
        ReservationInfoSuccess(movie)
      }
      val reply = movieActorRef ? CheckMovie(reservation)
      reply.map {
        case ReservationInfoSuccess(movie) => assert(true)
        case m => assert(false, s"Actor replied with unexpected message:$m")
      }
    }

  }
}
