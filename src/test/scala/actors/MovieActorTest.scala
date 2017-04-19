package actors

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import akka.pattern.ask
import models.Movie
import play.api.libs.json.Json

import scala.concurrent.duration.DurationInt
import scala.util.Success

class MovieActorTest extends TestKit(ActorSystem("TestKitUsageSpec"))
  with DefaultTimeout with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  "movie actor" should {
    "call movie create on repo for register message" in {
      val probe = TestProbe("movieActor")
      val future = probe.ref ? RegisterMovie(Movie(Json.obj()))
      probe.expectMsg(3 seconds, RegisterMovie(Movie(Json.obj())))
      probe.reply(RegisterSuccess)
      assert(future.isCompleted && future.value.contains(Success(RegisterSuccess)))
    }
  }

}
