package io.devmartynov.spiice.ui.addEditNote

import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.repository.note.NotesRepository
import java.util.*

/**
 * VM детальной карточки заметки
 * @param repository репозиторий заметок
 */
class NoteDetailViewModel(private val repository: NotesRepository) : ViewModel() {
    fun saveNote(note: Note): Boolean {
        return repository.updateNote(note)
    }

    fun getNote(noteId: UUID): Note? {
        return repository.getNote(noteId)
    }
}