package io.devmartynov.spiice.ui.addEditNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.repository.note.NotesRepository
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error

/**
 * VM детальной карточки заметки
 * @param repository репозиторий заметок
 */
class NoteDetailViewModel(private val repository: NotesRepository) : ViewModel() {
    private var _savingState = MutableLiveData<AsyncOperationState>(AsyncOperationState.Idle)
    val savingState: LiveData<AsyncOperationState> = _savingState

    fun saveNote(note: Note) {
        _savingState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateNote(note)
            _savingState.postValue(if (result) {
                AsyncOperationState.Success(true)
            } else {
                AsyncOperationState.Failure(Error("Error was occurred while saving note"))
            })
        }
    }
}