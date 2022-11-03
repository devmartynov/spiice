package io.devmartynov.spiice.db

import io.devmartynov.spiice.model.Note

/**
 * База данных заметок
 */
object NotesDataBase : DataBase<Note> {
    val notes: ArrayList<Note> = arrayListOf()
}