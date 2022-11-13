package io.devmartynov.spiice.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.devmartynov.spiice.ui.addEditNote.NoteDetailViewModel
import io.devmartynov.spiice.ui.notesList.NotesViewModel

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(application) as T
        }
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(application) as T
        }
        throw IllegalArgumentException("You've passed unrecognized VM class")
    }
}