package io.devmartynov.spiice.ui.notesList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.db.AppDatabase
import io.devmartynov.spiice.repository.note.NotesRepositoryImpl
import io.devmartynov.spiice.model.note.Note
import java.util.UUID

/**
 * VM списка заметок
 */
class NotesViewModel(application: Application): ViewModel() {
    private val repository = NotesRepositoryImpl(
        AppDatabase.getDatabase(application).noteDao()
    )

    private var _notes = arrayListOf<Note>()
    val notes = MutableLiveData<List<Note>>(_notes)

    fun loadNotes() {
        val list = repository
            .getNotes()
            .sortedBy { note -> note.createTime }
        _notes = ArrayList(list)
        updateLiveDataList()
    }

    fun deleteAllNotes(): Boolean {
        if (repository.deleteAllNotes()) {
            _notes.clear()
            updateLiveDataList()
            return true
        }
        return false
    }

    fun deleteNote(note: Note): Boolean {
        if (repository.deleteNote(note)) {
            _notes.removeIf { _note -> _note.id == note.id }
            updateLiveDataList()
            return true
        }
        return false
    }

    fun addNote(position: Int, note: Note): Boolean {
        if (repository.addNote(note)) {
            _notes.add(position, note)
            updateLiveDataList()
            return true
        }
        return false
    }

    fun getNote(noteId: UUID): Note? {
        return repository.getNote(noteId)
    }

    private fun updateLiveDataList() {
        notes.value = _notes
    }
}