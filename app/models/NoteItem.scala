package models

import play.api.libs.json.Json

case class NoteItem(id: Long, content: String, isDone: Boolean, noteId: Long)

object NoteItem {
  implicit val noteItemFormat = Json.format[NoteItem]
}
