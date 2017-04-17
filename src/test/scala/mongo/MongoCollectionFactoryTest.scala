package mongo

import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.duration.{DurationLong, FiniteDuration}

class MongoCollectionFactoryTest extends AsyncWordSpec with Matchers {

  private val collection = new MongoCollectionFactory().makeCollection("test-coll", Mongo.testDb)
  val atMost: FiniteDuration = 10.seconds

  "MongoCollectionFactory" should {

    "insert json object into collection" in {
      val doc = Json.obj("key" -> "val")
      Await.ready(collection.insert(doc), atMost).map(s => assert(true))
    }

    "find json object with given id in collection" in {
      val doc = Json.obj("_id" -> "123")
      Await.ready(collection.insert(doc), atMost)
      Await.ready(collection.findById("123").toFuture, atMost).map { docs =>
        (docs.head \ "_id").isDefined shouldEqual true
        (docs.head \ "_id").get.as[String] shouldEqual "123"
      }
    }
  }

}
