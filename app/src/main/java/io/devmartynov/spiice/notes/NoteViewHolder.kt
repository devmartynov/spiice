package io.devmartynov.spiice.notes

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.devmartynov.spiice.R
import io.devmartynov.spiice.model.Note

/**
 * Вьюхолдер для адаптера списка заметок
 */
class NoteViewHolder(
    private val view: View,
    private val noteClickListener: NotesAdapter.OnNoteClickListener,
    private val shareClickListener: NotesAdapter.OnShareClickListener,
) : ViewHolder(view) {
    fun bind(note: Note) {
        view.apply {
            view.setOnClickListener { noteClickListener.onClick(note) }
            findViewById<Button>(R.id.share).setOnClickListener { shareClickListener.onClick(note) }

            findViewById<TextView>(R.id.title).text = note.title
            findViewById<TextView>(R.id.content).text = note.content
            findViewById<TextView>(R.id.create_time).text = Note.timeAsDate(note.createTime)
        }
    }
}