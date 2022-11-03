package io.devmartynov.spiice.ui.notesList.notesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import io.devmartynov.spiice.R
import io.devmartynov.spiice.model.Note

typealias NoteClickHandler = (note: Note) -> Unit

/**
 * Адаптер для списка заметок
 * @param onNoteClick обработчик нажатия на элемент списка
 * @param onShareClick обработчик нажатия на кнопку шаринга
 */
class NotesAdapter(
    private val onNoteClick: NoteClickHandler,
    private val onShareClick: NoteClickHandler,
): RecyclerView.Adapter<NoteViewHolder>() {
    private var notes: List<Note> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            onNoteClick = onNoteClick,
            onShareClick = onShareClick,
            view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.note_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    /**
     * Добавляет новый список в адаптер
     * @param notes новый список заметок
     */
    fun setList(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    /**
     * Возвращает заметку из списка по позиции
     * @param position позиция заметки в списке(индекс)
     */
    fun getItem(position: Int): Note {
        return this.notes[position]
    }
}