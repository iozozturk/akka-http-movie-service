package mongo

import com.typesafe.config.{Config, ConfigFactory}
import org.mongodb.scala.{MongoClient, MongoDatabase}

object Mongo {

  val client: MongoClient = MongoClient()

  private val config: Config = ConfigFactory.load()

  private val dbName: String = config.getString("mongo.name")

  val db: MongoDatabase = client.getDatabase(dbName)
}
