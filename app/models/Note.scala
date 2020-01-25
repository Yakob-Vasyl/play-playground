package models

import play.api.libs.json._

case class Note(id: Long, name: String)

object Note {
  implicit val noteFormat = Json.format[Note]
}
