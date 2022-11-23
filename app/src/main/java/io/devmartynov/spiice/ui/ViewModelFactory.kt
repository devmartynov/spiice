package io.devmartynov.spiice.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.devmartynov.spiice.db.AppDatabase
import io.devmartynov.spiice.repository.note.NotesRepositoryImpl
import io.devmartynov.spiice.repository.user.UserRepositoryImpl
import io.devmartynov.spiice.ui.addEditNote.NoteDetailViewModel
import io.devmartynov.spiice.ui.auth.AuthViewModel
import io.devmartynov.spiice.ui.notesList.NotesViewModel
import io.devmartynov.spiice.ui.profile.ProfileViewModel

/**
 * Фабрика VM
 * Прокидывет репозитории во VM при создании ее экземпляра
 */
class ViewModelFactory(private val application: Application?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(
                NotesRepositoryImpl(
                    AppDatabase.get().noteDao()
                )
            ) as T
        }
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(
                NotesRepositoryImpl(
                    AppDatabase.get().noteDao()
                )
            ) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                UserRepositoryImpl(
                    AppDatabase.get().userDao()
                ),
                application!!
            ) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                UserRepositoryImpl(
                    AppDatabase.get().userDao()
                ),
                NotesRepositoryImpl(
                    AppDatabase.get().noteDao()
                )
            ) as T
        }
        throw IllegalArgumentException("You've passed unrecognized VM class")
    }
}