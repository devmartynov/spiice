package io.devmartynov.spiice.ui.notesList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.repository.NotesRepository
import io.devmartynov.spiice.repository.NotesRepositoryImpl
import io.devmartynov.spiice.model.Note
import java.util.UUID

class NotesViewModel: ViewModel() {
    private val repository: NotesRepository = NotesRepositoryImpl

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

    fun deleteNote(noteId: UUID): Boolean {
        if (repository.deleteNote(noteId)) {
            _notes.removeIf { note -> note.id == noteId }
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

    /**
     * Возвращает информация для шаринга заметки.
     * @param note заметка, которую шарим
     * @return инфа для шаринга в виде строки
     */
    fun getNoteSharingInfo(note: Note): String {
        return "${note.title}\n${note.content}"
    }

    private fun updateLiveDataList() {
        notes.value = _notes
    }
}