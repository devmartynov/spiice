package io.devmartynov.spiice.ui.notesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNotesBinding
import io.devmartynov.spiice.model.Note
import io.devmartynov.spiice.ui.addEditNote.AddEditNoteFragment
import io.devmartynov.spiice.ui.notesList.notesAdapter.NotesAdapter
import io.devmartynov.spiice.ui.auth.LoginFragment
import io.devmartynov.spiice.ui.notesList.noteDetailInfo.NoteDetailInfoFragment
import io.devmartynov.spiice.ui.notesList.noteMenu.NoteMenuFragment
import java.util.*

private const val NOTES_FRAGMENT_TAG = "FRAGMENT_TAG"

/**
 * Экран списка заметок
 */
class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        viewModel.notes.observe(viewLifecycleOwner) { notes -> updateUiList(notes) }

        binding.logout.setOnClickListener { logout() }
        binding.addNew.setOnClickListener { goToAddEditScreen() }
        binding.notesRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = NotesAdapter(
                onNoteClick = { note, _ -> showNoteDetailInfo(note) },
                onMenuClick = { note, position -> showNoteMenu(note, position) }
            )
        }
        ItemTouchHelper(getSimpleCallback()).attachToRecyclerView(binding.notesRecycler)
        viewModel.loadNotes()
    }

    /**
     * Коллбек для свайпа влево/вправо
     */
    private fun getSimpleCallback(): ItemTouchHelper.SimpleCallback {
        return SwiperCallback(
            context = requireContext(),
            config = object : SwiperCallback.Config {
                override fun deleteNote(position: Int) {
                    safeDeleteNoteByPosition(position)
                }
            },
            dragDirs = 0,
            swipeDirs = ItemTouchHelper.LEFT
        )
    }

    /**
     * Выход из аккаунта
     */
    private fun logout() {
        if (viewModel.deleteAllNotes()) {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }

    /**
     * Добавляет новый список в адаптер и обновляет весь список.
     * TODO: по уму вместо notifyDataSetChanged надо использовать более специфичные варианты, не всегда в onResume нужно обновлять весь список.
     */
    private fun updateUiList(notes: List<Note>) {
        (binding.notesRecycler.adapter as NotesAdapter).setList(notes)
    }

    /**
     * Переходит на экран создания/редактирования заметки.
     * Если noteId есть, значит это переход на экран редактирования, этот id передаем в arguments,
     * чтобы в AddEditNoteFragment заполнить поля формы значениями из заметки.
     * @param noteId id заметки
     */
    private fun goToAddEditScreen(noteId: UUID? = null) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, AddEditNoteFragment.newInstance(noteId))
            .addToBackStack(NOTES_FRAGMENT_TAG)
            .commit()
    }

    /**
     * Открывает боттом шит диалог - меню для заметки.
     * @param note заметка
     * @param position позиция заметки в адаптере
     */
    private fun showNoteMenu(note: Note, position: Int) {
        NoteMenuFragment()
            .apply {
                this.note = note
                goToEditScreen = { goToAddEditScreen(note.id) }
                safeDeleteNote = { safeDeleteNoteByPosition(position) }
            }
            .show(parentFragmentManager, NoteMenuFragment.TAG)
    }

    /**
     * Открывает боттом шит диалог - детальная информация заметки.
     * @param note заметка
     */
    private fun showNoteDetailInfo(note: Note) {
        NoteDetailInfoFragment.newInstance(note)
            .show(parentFragmentManager, NoteDetailInfoFragment.TAG)
    }

    /**
     * Удаляет заметку с возможностью восстановления
     * @param position позиция заметки в адаптере
     */
    private fun safeDeleteNoteByPosition(position: Int) {
        val deletedNote = (binding.notesRecycler.adapter as NotesAdapter).getItem(position)
        if (viewModel.deleteNote(deletedNote.id)) {
            Snackbar
                .make(
                    binding.notesRecycler,
                    getString(R.string.note_undo_remove_action_label),
                    Snackbar.LENGTH_SHORT
                )
                .setAction(getString(R.string.undo)) {
                    viewModel.addNote(position, deletedNote)
                }
                .show()
        }
    }
}