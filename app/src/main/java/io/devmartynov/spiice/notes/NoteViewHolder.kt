package io.devmartynov.spiice.notes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.devmartynov.spiice.R
import io.devmartynov.spiice.activities.NotesActivity
import io.devmartynov.spiice.model.Note

/**
 * Вьюхолдер для адаптера списка заметок
 */
class NoteViewHolder(
    private val view: View,
    private val noteClickListener: NotesActivity.OnNoteClickListener,
) : ViewHolder(view) {
    fun bind(note: Note) {
        view.apply {
            view.setOnClickListener { noteClickListener.onClick(note) }

            findViewById<TextView>(R.id.title).text = note.title
            findViewById<TextView>(R.id.content).text = note.content
            findViewById<TextView>(R.id.create_time).text = Note.timeAsDate(note.createTime)
        }
    }
}