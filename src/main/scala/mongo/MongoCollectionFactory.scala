package mongo

import com.google.inject.Singleton
import org.mongodb.scala.Completed
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class MongoCollectionFactory {

  def makeCollection(collName: String) = new MongoCollection(collName)

  class MongoCollection(val collName: String) {

    val db = Mongo.db

    def coll = db.getCollection(collName)

    def find(query: JsObject = Json.obj()): Future[Seq[JsObject]] = {
      coll.find(Document(query.toString())).map { d =>
        Json.parse(d.toJson()).as[JsObject]
      }.toFuture()
    }

    def findAndUpdate(query: JsObject, update: JsObject): Future[JsObject] = {
      coll.findOneAndUpdate(Document(query.toString()), Document(Json.obj("$set" -> update).toString())).map { d =>
        Json.parse(d.toJson()).as[JsObject]
      }.head()
    }

    def findOne(query: JsObject = Json.obj()): Future[JsObject] = {
      coll.find(Document(query.toString())).map { d =>
        Json.parse(d.toJson()).as[JsObject]
      }.head().filter(_ != null)
    }

    def findById(id: String) = {
      coll.find(Filters.eq("_id", id)).map { doc =>
        Json.parse(doc.toJson()).as[JsObject]
      }.head()
    }

    def insert(data: JsObject): Future[Completed] = {
      coll.insertOne(Document(data.toString())).head()
    }

  }

}
