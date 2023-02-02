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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNotesBinding
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.ui.notesList.noteDetailInfo.NoteDetailInfoFragment
import io.devmartynov.spiice.ui.notesList.notesAdapter.NotesAdapter
import io.devmartynov.spiice.utils.Callback
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState

/**
 * Экран списка заметок
 */
@AndroidEntryPoint
class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private val viewModel: NotesViewModel by viewModels()
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

        inputMethodManager = requireActivity()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        viewModel.gettingNotesState.observe(viewLifecycleOwner) { notesState ->
            when (notesState) {
                is AsyncOperationState.Loading -> {
                }
                is AsyncOperationState.Success -> {
                    val notes = notesState.data as List<Note>
                    updateUiList(notes)

                    // пользвоатель удалил заметку, надо показать снэкбар
                    if (viewModel.deletedNote != null) {
                        Snackbar
                            .make(
                                binding.notesRecycler,
                                getString(R.string.note_undo_remove_action_label),
                                Snackbar.LENGTH_SHORT
                            )
                            .setAction(getString(R.string.undo)) {
                                viewModel.addNote(
                                    viewModel.deletedNotePosition!!,
                                    viewModel.deletedNote!!
                                )
                            }
                            .show()
                    }
                }
                is AsyncOperationState.Failure -> {
                }
                is AsyncOperationState.Idle -> {
                }
            }
        }

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
     * Открывает боттом шит диалог - меню для заметки.
     * @param note заметка
     * @param position позиция заметки в адаптере
     */
    private fun showNoteMenu(note: Note, position: Int) {
        val action = NotesFragmentDirections.actionNotesFragmentToNoteMenuFragment(
            note = note,
            safeDeleteNote = object : Callback {
                override fun invoke() {
                    safeDeleteNoteByPosition(position)
                }
            }
        )
        findNavController().navigate(action)
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
        viewModel.setDeletedNote(deletedNote, position)
        viewModel.deleteNote(deletedNote)
    }
}