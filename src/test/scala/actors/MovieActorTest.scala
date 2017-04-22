package actors

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit}
import models.Movie
import org.mockito.Mockito.when
import org.mongodb.scala.Completed
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncWordSpecLike, Matchers}
import play.api.libs.json.Json
import repos.MovieRepo

import scala.concurrent.Future

class MovieActorTest extends TestKit(ActorSystem("TestKitUsageSpec"))
  with DefaultTimeout with ImplicitSender
  with AsyncWordSpecLike with Matchers with MockitoSugar {

  val movieRepo = mock[MovieRepo]
  val movie = Movie(Json.obj("imdbId" -> "tt01"))

  val movieActorRef = TestActorRef(new MovieActor(movieRepo))
  val movieActor = movieActorRef.underlyingActor

  "movie actor" should {

    "reply with RegisterSuccess message when movie does not exist" in {
//      when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future.failed(new Exception())
      when(movieRepo.createMovie(movie)) thenReturn Future {
        new Completed
      }

      val reply = movieActorRef ? RegisterMovie(movie)
      reply.map {
        case RegisterSuccess => assert(true)
        case m => assert(false, s"Actor replied with unexpected message:$m")
      }


    }

    //    "reply with AlreadyRegistered message when movie registered before" in {
    //      val probe = TestProbe("movieActor")
    //      when(movieRepo.findMovieByImdbId("tt01")) thenReturn Future {
    //        movie
    //      }
    //      when(movieRepo.createMovie(movie)) thenReturn Future.failed(new Exception())
    //      val future = probe.ref ? RegisterMovie(movie)
    //      probe.expectMsg(3 seconds, RegisterMovie(movie))
    //      probe.reply(AlreadyRegistered)
    //      assert(future.isCompleted && future.value.contains(Success(AlreadyRegistered)))
    //    }


  }

}
