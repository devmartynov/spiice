package io.devmartynov.spiice.repository

import io.devmartynov.spiice.db.NotesDataBase
import io.devmartynov.spiice.model.Note
import java.util.*

/**
 * Репозиторий заметок.
 */
object NotesRepositoryImpl: NotesRepository {
    private var notesDB = NotesDataBase

    init {
        val note1 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 1",
            content = "Контент заметки 1",
            createTime = System.currentTimeMillis(),
            scheduleTime = System.currentTimeMillis(),
        )
        val note2 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 2",
            content = "Контент заметки 2",
            createTime = System.currentTimeMillis() + 1,
            scheduleTime = Calendar.getInstance().apply {
                time = Date(System.currentTimeMillis())
                add(Calendar.HOUR_OF_DAY, 32)
            }.time.time
        )
        val note3 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 3",
            content = "Контент заметки 3",
            createTime = System.currentTimeMillis() + 2
        )
        val note4 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 4",
            content = "Контент заметки 4",
            createTime = System.currentTimeMillis() + 3
        )
        val note5 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 5",
            content = "Контент заметки 5",
            createTime = System.currentTimeMillis() + 4
        )
        val note6 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 6",
            content = "Контент заметки 6",
            createTime = System.currentTimeMillis() + 5
        )
        val note7 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 7",
            content = "Контент заметки 7",
            createTime = System.currentTimeMillis() + 6
        )
        val note8 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 8",
            content = "Контент заметки 8",
            createTime = System.currentTimeMillis() + 7
        )
        val note9 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 9",
            content = "Контент заметки 9",
            createTime = System.currentTimeMillis() + 8
        )
        val note10 = Note(
            id = UUID.randomUUID(),
            title = "Заметка 10",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 9
        )
        notesDB.notes.add(note1)
        notesDB.notes.add(note2)
        notesDB.notes.add(note3)
        notesDB.notes.add(note4)
        notesDB.notes.add(note5)
        notesDB.notes.add(note6)
        notesDB.notes.add(note7)
        notesDB.notes.add(note8)
        notesDB.notes.add(note9)
        notesDB.notes.add(note10)
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 11",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 10
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 12",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 11
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 13",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 12
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 14",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 13
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 15",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 14
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 16",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 15
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 17",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 16
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 18",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 17
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 19",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 18
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 20",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 19
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 21",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 20
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 22",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 21
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 23",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 22
        ))
        notesDB.notes.add(Note(
            id = UUID.randomUUID(),
            title = "Заметка 24",
            content = "Контент заметки 10",
            createTime = System.currentTimeMillis() + 23
        ))
    }

    override fun addNote(note: Note): Boolean {
        if (isExisted(note.id)) {
            return false
        }
        notesDB.notes.add(note)
        return true
    }

    override fun updateNote(note: Note): Boolean {
        if (!isExisted(note.id)) {
            return false
        }
        notesDB.notes.map { dbNote -> if (dbNote.id == note.id) note else dbNote }
        val replaceNote = findNoteById(note.id)

        if (replaceNote != null) {
            val index = notesDB.notes.indexOf(replaceNote)
            notesDB.notes[index] = note
        }

        return true
    }

    override fun saveNote(note: Note): Boolean {
        if (!addNote(note)) {
            return updateNote(note)
        }
        return true
    }

    override fun deleteNote(noteId: UUID): Boolean {
        if (!isExisted(noteId)) {
            return false
        }
        notesDB.notes.removeIf { note -> note.id == noteId }
        return true
    }

    override fun getNotes(): List<Note> {
        return notesDB.notes
    }

    override fun deleteAllNotes(): Boolean {
        notesDB.notes.clear()
        return true
    }

    override fun getNote(noteId: UUID): Note? {
        return findNoteById(noteId)
    }

    /**
     * Проверяет наличие заметки в репозитории по id
     * @param noteId id заметки
     * @return true если заметка есть в хранилище, иначе false
     */
    private fun isExisted(noteId: UUID): Boolean {
        return findNoteById(noteId) != null
    }

    /**
     * Ищет заметку по переданному id
     * @param noteId id заметки
     * @return заметку если в базе есть заметки с таким id, иначе false
     */
    private fun findNoteById(noteId: UUID): Note? {
        return notesDB.notes.toList().firstOrNull { note -> note.id == noteId }
    }
}