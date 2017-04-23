package init

import akka.event.{Logging, LoggingAdapter}
import com.mongodb.client.model.Indexes.ascending
import common.MovieSystem
import mongo.Mongo
import org.mongodb.scala.model.IndexOptions

object IndexMapping extends MovieSystem {

  def defineIndexes(): Unit = {
    logger.info("Creating indices")
    val options: IndexOptions = new IndexOptions().unique(true)
    Mongo.db.getCollection("movies").createIndex(ascending("imdbId"), options).subscribe((s: String) => {
      logger.info("ImdbId index created on Movies collection")
    })
  }

  override val logger: LoggingAdapter = Logging(system, getClass)
}
