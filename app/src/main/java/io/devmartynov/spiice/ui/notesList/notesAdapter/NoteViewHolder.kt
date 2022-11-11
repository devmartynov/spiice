package io.devmartynov.spiice.ui.notesList.notesAdapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.devmartynov.spiice.R
import io.devmartynov.spiice.model.Note
import io.devmartynov.spiice.model.NoteType

/**
 * Вьюхолдер для адаптера списка заметок
 * @param view view элемента списка
 * @param onNoteClick обработчик нажатия на элемент списка
 * @param onMenuClick обработчик нажатия на кнопку открытия меню
 */
class NoteViewHolder(
    private val view: View,
    private val onNoteClick: NoteClickHandler,
    private val onMenuClick: NoteClickHandler,
) : ViewHolder(view) {
    /**
     * Устанавливает значения во вьюхи элемента списка
     * @param note заметка
     */
    fun bind(note: Note, position: Int) {
        view.apply {
            setUpScheduleIndicator(note.noteType)
            findViewById<ImageView>(R.id.schedule_time_indicator)
            findViewById<TextView>(R.id.title).text = note.title
            findViewById<TextView>(R.id.content).text = note.content
            findViewById<TextView>(R.id.create_time).text = Note.timeAsDate(note.createTime)

            setOnClickListener { onNoteClick(note, position) }
            findViewById<Button>(R.id.menu_toggle).setOnClickListener { onMenuClick(note, position) }
        }
    }

    /**
     * Устанавливает индикатор по запланированному времени заметки.
     * @param noteType тип заметки
     */
    private fun setUpScheduleIndicator(noteType: NoteType) {
        val indicatorView = view.findViewById<ImageView>(R.id.schedule_time_indicator)

        if (noteType == NoteType.REGULAR) {
            indicatorView.visibility = View.GONE
            return
        }

        val drawableIndicatorIconResId = when (noteType) {
            NoteType.SCHEDULED_EXPIRED -> R.drawable.ic_expired
            NoteType.SCHEDULED_TODAY -> R.drawable.ic_today
            else -> R.drawable.ic_future
        }

        view.findViewById<ImageView>(R.id.schedule_time_indicator).apply {
            visibility = View.VISIBLE
            setImageResource(drawableIndicatorIconResId)
        }
    }
}