package io.devmartynov.spiice.ui.notesList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.model.user.UserPreferences
import io.devmartynov.spiice.repository.note.NotesRepository
import java.util.UUID

/**
 * VM списка заметок
 * @param repository репозиторий заметок
 */
class NotesViewModel(private val repository: NotesRepository) : ViewModel() {
    private var isInitSearch = true
    private var _notesBeforeFilter = arrayListOf<Note>()
    private var _notes = arrayListOf<Note>()
    val notes = MutableLiveData<List<Note>>(_notes)

    fun loadNotes() {
        val list = repository
            .getUserNotes(UserPreferences.get().userId)
            .sortedByDescending { note -> note.createTime }
        _notes = ArrayList(list)
        updateLiveDataList()
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

    fun searchNotes(value: String) {
        if (isInitSearch) {
            _notesBeforeFilter = arrayListOf()
            _notesBeforeFilter.addAll(_notes)
            isInitSearch = false
        }
        _notes = _notesBeforeFilter.filter { note ->
            note.title.contains(value, ignoreCase = true)
                    || note.content.contains(value, ignoreCase = true)
        } as ArrayList<Note>
        updateLiveDataList()
    }

    fun resetSearch() {
        _notesBeforeFilter = arrayListOf()
        _notes.addAll(_notesBeforeFilter)
        _notesBeforeFilter = arrayListOf()
        updateLiveDataList()
        isInitSearch = true
    }

    private fun updateLiveDataList() {
        notes.value = _notes
    }
}