package io.devmartynov.spiice.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.devmartynov.spiice.databinding.ActivityNotesBinding
import io.devmartynov.spiice.model.Note
import io.devmartynov.spiice.notes.NotesAdapter
import io.devmartynov.spiice.notes.SwiperCallback
import io.devmartynov.spiice.view.NotesViewModel
import java.util.*

/**
 * Экран списка заметок
 */
class NotesActivity: AppCompatActivity() {
    private val simpleCallback = SwiperCallback(
        context = this,
        config = object : SwiperCallback.Config {
            override fun updateUi() {
                updateUiList()
            }
            override fun deleteNote(noteId: UUID): Boolean = notesViewModel.deleteNote(noteId)
            override fun addNote(note: Note): Boolean = notesViewModel.addNote(note)
            override fun getRecycler(): RecyclerView = binding.notesRecycler
        },
        dragDirs = 0,
        swipeDirs = ItemTouchHelper.LEFT
    )

    private lateinit var binding: ActivityNotesBinding

    private val notesViewModel: NotesViewModel by lazy {
        ViewModelProvider(this)[NotesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.logout.setOnClickListener {
            notesViewModel.deleteAllNotes()
            startActivity(
                Intent(this@NotesActivity, LoginActivity::class.java)
            )
        }
        binding.addNew.setOnClickListener { goToAddEditScreen() }
        binding.notesRecycler.apply {
            layoutManager = LinearLayoutManager(this@NotesActivity)
            adapter = NotesAdapter(
                notes = notesViewModel.getNotes(),
                noteClickListener = object: NotesAdapter.OnNoteClickListener {
                override fun onClick(note: Note) {
                    goToAddEditScreen(note.id)
                }
            })
        }
        (binding.notesRecycler.adapter as NotesAdapter).setList(notesViewModel.getNotes())
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.notesRecycler)
    }

    /**
     * Обновляем список
     */
    override fun onResume() {
        super.onResume()
        updateUiList()
    }

    /**
     * Добавляет новый список в адаптер и обновляет весь список.
     * TODO: по уму вместо notifyDataSetChanged надо использовать более специфичные варианты, не всегда в onResume нужно обновлять весь список.
     */
    private fun updateUiList() {
        (binding.notesRecycler.adapter as NotesAdapter).setList(notesViewModel.getNotes())
        (binding.notesRecycler.adapter as NotesAdapter).notifyDataSetChanged()
    }

    /**
     * Переходит на экран создания/редактирования заметки.
     * Если noteId есть, значит это переход на экран редактирования, этот id передаем в extra,
     * чтобы в AddEditNoteActivity заполнить поля формы значениями из заметки.
     * @param noteId id заметки
     */
    private fun goToAddEditScreen(noteId: UUID? = null) {
        val intent = Intent(this@NotesActivity, AddEditNoteActivity::class.java)

        if (noteId != null) {
            intent.putExtra(EXTRA_NOTE_ID, noteId)
        }

        startActivity(intent)
    }
}