package io.devmartynov.spiice.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devmartynov.spiice.model.user.UserPreferences
import io.devmartynov.spiice.repository.note.NotesRepository
import io.devmartynov.spiice.repository.user.UserRepository
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * VM профиля
 * @param userRepository репозиторий пользователя
 * @param notesRepository репозиторий заметок
 */
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val notesRepository: NotesRepository
) : ViewModel() {
    private val userPreferences = UserPreferences.get()
    private var _gettingNotesCountState = MutableLiveData<AsyncOperationState>(AsyncOperationState.Idle)
    val gettingNotesCountState: LiveData<AsyncOperationState> = _gettingNotesCountState
    private var _deletingProfileState = MutableLiveData<AsyncOperationState>(AsyncOperationState.Idle)
    val deletingProfileState: LiveData<AsyncOperationState> = _deletingProfileState

    companion object {
        const val ZERO_NOTES = 0L
    }

    fun getUserFullName(): String {
        return userPreferences.fullName
    }

    fun signOut() {
        userPreferences.token = null
    }

    fun deleteProfile() {
        deleteAllNotes()
        _deletingProfileState.value = AsyncOperationState.Loading
        viewModelScope.launch {
            userRepository.deleteUser(userPreferences.userId)
            _deletingProfileState.postValue(AsyncOperationState.Success(0))
            signOut()
        }
    }

    fun getNotesCount() {
        _gettingNotesCountState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val count = notesRepository.getUserNotesCount(userPreferences.userId)
            _gettingNotesCountState.postValue(AsyncOperationState.Success(count))
        }
    }

    fun deleteAllNotes() {
        _gettingNotesCountState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteAllUserNotes(userPreferences.userId)
            _gettingNotesCountState.postValue(AsyncOperationState.Success(ZERO_NOTES))
        }
    }
}