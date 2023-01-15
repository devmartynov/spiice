package io.devmartynov.spiice.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.devmartynov.spiice.repository.user.UserPreferencesRepository
import io.devmartynov.spiice.repository.note.NotesRepository
import io.devmartynov.spiice.repository.user.UserRepository
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

/**
 * VM профиля
 * @param userRepository репозиторий пользователя
 * @param notesRepository репозиторий заметок
 * @param userPreferences преференцы пользователя
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val notesRepository: NotesRepository,
    private val userPreferences: UserPreferencesRepository,
) : ViewModel() {
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
            try {
                userRepository.deleteUser(userPreferences.userId)
                _deletingProfileState.postValue(AsyncOperationState.Success(0))
                signOut()
            } catch (e: java.lang.Exception) {
                _deletingProfileState.postValue(AsyncOperationState.Failure(Error(e)))
            }
        }
    }

    fun getNotesCount() {
        _gettingNotesCountState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = notesRepository.getUserNotesCount(userPreferences.userId)
                _gettingNotesCountState.postValue(AsyncOperationState.Success(count))
            } catch (e: java.lang.Exception) {
                _gettingNotesCountState.postValue(AsyncOperationState.Failure(Error(e)))
            }
        }
    }

    fun deleteAllNotes() {
        _gettingNotesCountState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                notesRepository.deleteAllUserNotes(userPreferences.userId)
                _gettingNotesCountState.postValue(AsyncOperationState.Success(ZERO_NOTES))
            } catch (e: java.lang.Exception) {
                _gettingNotesCountState.postValue(AsyncOperationState.Failure(Error(e)))
            }
        }
    }
}