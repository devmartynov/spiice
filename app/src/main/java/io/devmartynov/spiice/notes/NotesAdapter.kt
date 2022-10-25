package io.devmartynov.spiice.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.devmartynov.spiice.R
import io.devmartynov.spiice.activities.NotesActivity
import io.devmartynov.spiice.model.Note

/**
 * Адаптер для списка заметок
 */
class NotesAdapter(
    private var notes: List<Note>,
    private val noteClickListener: OnNoteClickListener,
): RecyclerView.Adapter<NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false),
            noteClickListener
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    /**
     * Добавляет новый список в адаптер
     * @param notes новый список заметок
     */
    fun setList(notes: List<Note>) {
        this.notes = notes
    }

    /**
     * Возвращает заметку из списка по позиции
     * @param position позиция заметки в списке(индекс)
     */
    fun getItem(position: Int): Note {
        return this.notes[position]
    }

    /**
     * Слушатель события нажатия по заметке в списке.
     */
    interface OnNoteClickListener {
        fun onClick(note: Note)
    }
}