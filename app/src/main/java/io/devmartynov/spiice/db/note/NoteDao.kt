package io.devmartynov.spiice.db.note

import androidx.room.*
import io.devmartynov.spiice.model.note.Note
import java.util.UUID

/**
 * DAO для заметок
 */
@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note WHERE userCreatorId=(:userId)")
    suspend fun deleteAllUserNotes(userId: UUID)

    @Query("SELECT * FROM note WHERE userCreatorId=(:userId)")
    suspend fun getUserNotes(userId: UUID): List<Note>

    @Query("SELECT COUNT(id) FROM note WHERE userCreatorId=(:userId)")
    suspend fun getUserNotesCount(userId: UUID): Long

}