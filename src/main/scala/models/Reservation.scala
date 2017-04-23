package models

import play.api.libs.json.JsObject

case class Reservation(json: JsObject) {
  lazy val imdbId: String = (json \ "imdbId").as[String]
  lazy val screenId: String = (json \ "screenId").as[String]
}
