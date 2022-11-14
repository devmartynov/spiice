package io.devmartynov.spiice.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.db.AppDatabase
import io.devmartynov.spiice.model.user.UserPreferences
import io.devmartynov.spiice.repository.note.NotesRepositoryImpl
import io.devmartynov.spiice.repository.user.UserRepositoryImpl

class ProfileViewModel(application: Application) : ViewModel() {
    private val userPreferences = UserPreferences.get()
    private val userRepository = UserRepositoryImpl(
        AppDatabase.getDatabase(application).userDao()
    )
    private val noteRepository = NotesRepositoryImpl(
        AppDatabase.getDatabase(application).noteDao()
    )

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
        return noteRepository.getUserNotesCount(userPreferences.userId)
    }

    fun deleteAllNotes() {
        noteRepository.deleteAllUserNotes(userPreferences.userId)
    }
}