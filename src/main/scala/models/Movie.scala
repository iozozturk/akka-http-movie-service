package models

import play.api.libs.json.JsObject

case class Movie(json: JsObject) {

  lazy val _id = (json \ "_id").as[String]
  lazy val imdbId = (json \ "imdbId").as[String]
  lazy val title = (json \ "title").as[String]
  lazy val availableSeats = (json \ "availableSeats").as[Int]
  lazy val reservedSeats = (json \ "reservedSeats").as[Int]
  lazy val screenId = (json \ "screenId").as[String]

}