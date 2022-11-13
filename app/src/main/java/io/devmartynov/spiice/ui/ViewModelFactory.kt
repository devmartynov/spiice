package io.devmartynov.spiice.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.devmartynov.spiice.ui.addEditNote.NoteDetailViewModel
import io.devmartynov.spiice.ui.auth.AuthViewModel
import io.devmartynov.spiice.ui.notesList.NotesViewModel

/**
 * Фабрика VM
 * Прокидывет application во VM при создании ее экземпляра
 * @param application контекст приложения
 */
class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(application) as T
        }
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(application) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(application) as T
        }
        throw IllegalArgumentException("You've passed unrecognized VM class")
    }
}