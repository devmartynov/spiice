package io.devmartynov.spiice.ui.profile

import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.model.user.UserPreferences
import io.devmartynov.spiice.repository.note.NotesRepository
import io.devmartynov.spiice.repository.user.UserRepository

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
        userRepository.deleteUser(userPreferences.userId)
        signOut()
    }

    fun getNotesCount(): Long {
        return notesRepository.getUserNotesCount(userPreferences.userId)
    }

    fun deleteAllNotes() {
        notesRepository.deleteAllUserNotes(userPreferences.userId)
    }
}