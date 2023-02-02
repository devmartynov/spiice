package io.devmartynov.spiice.ui.addEditNote

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
import java.lang.Error
import java.util.UUID
import javax.inject.Inject

/**
 * VM детальной карточки заметки
 * @param repository репозиторий заметок,
 * @param userPreferencesRepository преференцы пользователя
 */
@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private var _savingState = MutableLiveData<AsyncOperationState>(AsyncOperationState.Idle)
    val savingState: LiveData<AsyncOperationState> = _savingState
    val userId: UUID
        get() = userPreferencesRepository.userId

    fun saveNote(note: Note) {
        _savingState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.updateNote(note)
                _savingState.postValue(
                    if (result) {
                        AsyncOperationState.Success(true)
                    } else {
                        AsyncOperationState.Failure(Error("Error was occurred while saving note"))
                    }
                )
            } catch (e: java.lang.Exception) {
                _savingState.postValue(AsyncOperationState.Failure(Error(e)))
            }
        }
    }
}