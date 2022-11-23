package io.devmartynov.spiice.ui.notesList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNotesBinding
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.ui.ViewModelFactory
import io.devmartynov.spiice.ui.addEditNote.AddEditNoteFragment
import io.devmartynov.spiice.ui.notesList.noteDetailInfo.NoteDetailInfoFragment
import io.devmartynov.spiice.ui.notesList.noteMenu.NoteMenuFragment
import io.devmartynov.spiice.ui.notesList.notesAdapter.NotesAdapter
import java.util.*

private const val NOTES_FRAGMENT_TAG = "FRAGMENT_TAG"

/**
 * Экран списка заметок
 */
class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private val viewModel: NotesViewModel by viewModels { ViewModelFactory(null) }
    private var isSearchFieldOpened = false
    private var inputMethodManager: InputMethodManager? = null

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

        val activity = requireActivity()

        inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val bottomNav = activity.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.VISIBLE

        viewModel.notes.observe(viewLifecycleOwner) { notes -> updateUiList(notes) }

        binding.toggleSearchField.setOnClickListener { toggleSearchField() }
        binding.searchField.doAfterTextChanged { viewModel.searchNotes(it.toString()) }
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
     * Переключает видимость поля поиска.
     * При появлении поля поиска фокус падает на него + открывается клавиатура для ввода
     * Когда прячем поиск клавиатура пропадает
     */
    private fun toggleSearchField() {
        if (isSearchFieldOpened) {
            binding.toggleSearchField.setCompoundDrawablesWithIntrinsicBounds(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_search, null),
                null,
                null,
                null
            )
            binding.searchField.apply {
                editableText.clear()
                clearFocus()
                inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
                visibility = View.GONE
            }
            binding.screenTitle.visibility = View.VISIBLE
            viewModel.resetSearch()
            isSearchFieldOpened = false
        } else {
            binding.toggleSearchField.setCompoundDrawablesWithIntrinsicBounds(
                ResourcesCompat.getDrawable(resources, R.drawable.ic_close, null),
                null,
                null,
                null
            )
            binding.searchField.apply {
                visibility = View.VISIBLE
                requestFocus()
                inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
            binding.screenTitle.visibility = View.GONE
            isSearchFieldOpened = true
        }
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
        if (viewModel.deleteNote(deletedNote)) {
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