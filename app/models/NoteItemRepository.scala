package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class NoteItemRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
   val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class NoteItemTable(tag: Tag) extends Table[NoteItem](tag, "note_item") {


    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    /** The name column */
    def content = column[String]("content")

    def isDone = column[Boolean]("is_done")

    def noteId = column[Long]("note_id")

    override def * = (id, content, isDone, noteId) <> ((NoteItem.apply _).tupled, NoteItem.unapply)
  }

  val tableQuery = TableQuery[NoteItemTable]

  def create(content: String, isDone: Boolean, noteId: Long): Future[NoteItem] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (tableQuery.map(p => (p.content, p.isDone, p.noteId))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning tableQuery.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((contentIsDoneNoteID, id) => NoteItem(id = id, content = contentIsDoneNoteID._1, isDone = contentIsDoneNoteID._2, noteId = contentIsDoneNoteID._3))
      // And finally, insert the person into the database
      ) += (content,isDone, noteId)
  }
}
