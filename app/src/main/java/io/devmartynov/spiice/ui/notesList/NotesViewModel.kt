package io.devmartynov.spiice.ui.notesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.repository.note.NotesRepository
import io.devmartynov.spiice.repository.user.UserPreferencesRepository
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VM списка заметок
 * @param notesRepository репозиторий заметок,
 * @param userPreferencesRepository преференцы пользователя
 */
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private var isInitSearch = true
    private var _notesBeforeFilter = arrayListOf<Note>()
    private var _notes = arrayListOf<Note>()
    private var _gettingNotesState = MutableLiveData<AsyncOperationState>(AsyncOperationState.Idle)
    var deletedNote: Note? = null
    var deletedNotePosition: Int? = null
    val gettingNotesState: LiveData<AsyncOperationState> = _gettingNotesState

    fun loadNotes() {
       _gettingNotesState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val list = notesRepository
                .getUserNotes(userPreferencesRepository.userId)
                .sortedByDescending { note -> note.createTime }
            _notes = ArrayList(list)
            updateGettingNotesState(AsyncOperationState.Success(_notes))
        }
    }

    fun deleteNote(note: Note) {
        _gettingNotesState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNote(note)
            _notes.removeIf { _note -> _note.id == note.id }
            updateGettingNotesState(AsyncOperationState.Success(_notes))
        }
    }

    fun addNote(position: Int, note: Note) {
        _gettingNotesState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.addNote(note)
            _notes.add(position, note)
            setDeletedNote(null, null)
            updateGettingNotesState(AsyncOperationState.Success(_notes))
        }
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
        updateGettingNotesState(AsyncOperationState.Success(_notes))
    }

    fun resetSearch() {
        _notesBeforeFilter = arrayListOf()
        _notes.addAll(_notesBeforeFilter)
        _notesBeforeFilter = arrayListOf()
        updateGettingNotesState(AsyncOperationState.Success(_notes))
        isInitSearch = true
    }
    fun setDeletedNote(note: Note?, position: Int?) {
       deletedNote = note
       deletedNotePosition = position
    }
    private fun updateGettingNotesState(value: AsyncOperationState) {
       _gettingNotesState.postValue(value)
    }
}