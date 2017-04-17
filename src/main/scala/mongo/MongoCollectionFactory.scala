package mongo

import com.google.inject.Singleton
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters
import org.mongodb.scala.{Completed, MongoDatabase}
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.Future

@Singleton
class MongoCollectionFactory {

  def makeCollection(collName: String, db: MongoDatabase = Mongo.db) = new MongoCollection(collName, db)

  class MongoCollection(val collName: String, db: MongoDatabase) {

    def coll = db.getCollection(collName)

    def findById(id: String) = {
      coll.find(Filters.eq("_id", id)).map { doc =>
        Json.parse(doc.toJson()).as[JsObject]
      }
    }

    def insert(data: JsObject): Future[Completed] = {
      coll.insertOne(Document(data.toString())).head()
    }

  }

}