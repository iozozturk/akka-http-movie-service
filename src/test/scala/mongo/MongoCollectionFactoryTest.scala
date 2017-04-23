package mongo

import common.FreshDbContext
import org.scalatest.{AsyncWordSpec, Matchers}
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.duration.{DurationLong, FiniteDuration}

class MongoCollectionFactoryTest extends AsyncWordSpec with Matchers with FreshDbContext {

  private val collection = new MongoCollectionFactory().makeCollection("test-coll")
  val atMost: FiniteDuration = 10.seconds

  "MongoCollectionFactory" should {

    "insert entity into collection" in {
      val doc = Json.obj("key" -> "val")
      Await.ready(collection.insert(doc), atMost).map(s => assert(true))
    }

    "find entity with given id in collection" in {
      val doc = Json.obj("_id" -> "123")
      Await.ready(collection.insert(doc), atMost)
      Await.ready(collection.findById("123"), atMost).map { docs =>
        (docs \ "_id").isDefined shouldEqual true
        (docs \ "_id").get.as[String] shouldEqual "123"
      }
    }

    "find entity with custom query" in {
      val doc = Json.obj("_id" -> "123")
      val query = Json.obj("_id" -> "123")
      Await.ready(collection.insert(doc), atMost)

      Await.ready(collection.find(query), atMost).map { docs =>
        (docs.head \ "_id").isDefined shouldEqual true
        (docs.head \ "_id").get.as[String] shouldEqual "123"
      }
    }

    "find only one entity with custom query" in {
      val doc = Json.obj("_id" -> "123")
      val query = Json.obj("_id" -> "123")
      Await.ready(collection.insert(doc), atMost)

      Await.ready(collection.findOne(query), atMost).map { doc =>
        (doc \ "_id").isDefined shouldEqual true
        (doc \ "_id").get.as[String] shouldEqual "123"
      }
    }

    "find and update entity" in {
      val doc = Json.obj("_id" -> "123", "title" -> "shawshank")
      val query = Json.obj("_id" -> "123")
      val update = Json.obj("title" -> "rock")
      Await.ready(collection.insert(doc), atMost)

      Await.ready(collection.findAndUpdate(query, update), atMost).map { doc =>
        Await.ready(collection.findOne(query), atMost).map { doc =>
          (doc \ "title").isDefined shouldEqual true
          (doc \ "title").get.as[String] shouldEqual "rock"
        }
      }.flatten
    }
  }

}
