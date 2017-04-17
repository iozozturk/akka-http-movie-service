package mongo

import com.typesafe.config.{Config, ConfigFactory}
import org.mongodb.scala.{MongoClient, MongoDatabase}

object Mongo {

  val client: MongoClient = MongoClient()

  private val config: Config = ConfigFactory.load(this.getClass.getClassLoader, "application.conf")

  private val dbName: String = config.getString("mongo.name")

  val db: MongoDatabase = client.getDatabase(dbName)
  val testDb: MongoDatabase = client.getDatabase("test-movie-db")

}
