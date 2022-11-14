package io.devmartynov.spiice.db.note

import androidx.room.*
import io.devmartynov.spiice.model.note.Note
import java.util.UUID

/**
 * DAO для заметок
 */
@Dao
interface NoteDao {
    @Query("SELECT * FROM note WHERE id=(:id)")
    fun getNote(id: UUID): Note?

    @Query("SELECT * FROM note")
    fun getNotes(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("DELETE FROM note")
    fun deleteAllNotes()

    @Query("DELETE FROM note WHERE userCreatorId=(:userId)")
    fun deleteAllUserNotes(userId: UUID)

    @Query("SELECT * FROM note WHERE userCreatorId=(:userId)")
    fun getUserNotes(userId: UUID): List<Note>

    @Query("SELECT COUNT(id) FROM note WHERE userCreatorId=(:userId)")
    fun getUserNotesCount(userId: UUID): Long

}