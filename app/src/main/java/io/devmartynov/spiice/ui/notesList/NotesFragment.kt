package io.devmartynov.spiice.ui.notesList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNotesBinding
import io.devmartynov.spiice.model.Note
import io.devmartynov.spiice.ui.addEditNote.AddEditNoteFragment
import io.devmartynov.spiice.ui.notesList.notesAdapter.NotesAdapter
import io.devmartynov.spiice.ui.auth.LoginFragment
import java.util.*

private const val NOTES_FRAGMENT_TAG = "FRAGMENT_TAG"
private const val SHARE_CONTENT_TYPE = "text/plain"

/**
 * Экран списка заметок
 */
class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding

    private val viewModel: NotesViewModel by lazy {
        ViewModelProvider(this)[NotesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("UUU", "ON_CREATE_VIEW-----")

        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("UUU", "ON_VIEW_CREATED-----")


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
                onNoteClick = { note -> goToAddEditScreen(note.id) },
                onShareClick = { note -> shareNote(note) }
            )
        }
        ItemTouchHelper(getSimpleCallback()).attachToRecyclerView(binding.notesRecycler)
        viewModel.loadNotes()
    }

    private fun getSimpleCallback(): ItemTouchHelper.SimpleCallback {
        return SwiperCallback(
            context = requireContext(),
            config = object : SwiperCallback.Config {
                override fun deleteNote(noteId: UUID): Boolean = viewModel.deleteNote(noteId)
                override fun addNote(position: Int, note: Note): Boolean = viewModel.addNote(position, note)
                override fun getRecycler(): RecyclerView = binding.notesRecycler
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
     * Формирует интент для отправки текста заметки и запускает его.
     * @param note заметка для шаринга
     */
    private fun shareNote(note: Note) {
        Intent(Intent.ACTION_SEND)
            .apply {
                type = SHARE_CONTENT_TYPE
                putExtra(Intent.EXTRA_TEXT, viewModel.getNoteSharingInfo(note))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            }
            .also { intent ->
                startActivity(
                    Intent.createChooser(intent, getString(R.string.share_dialog_title))
                )
            }
    }
}