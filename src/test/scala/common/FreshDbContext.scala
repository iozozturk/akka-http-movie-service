package common

import mongo.Mongo
import org.mongodb.scala.Completed

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationLong

trait FreshDbContext {

  val result: Future[Completed] = Await.ready(Mongo.db.drop().head(), 5.seconds)

}
