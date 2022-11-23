package io.devmartynov.spiice.ui.addEditNote

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.devmartynov.spiice.utils.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.utils.validation.ValidationResult
import io.devmartynov.spiice.databinding.FragmentAddEditNoteBinding
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.model.user.UserPreferences
import io.devmartynov.spiice.ui.ViewModelFactory
import io.devmartynov.spiice.ui.notesList.NotesFragment
import io.devmartynov.spiice.validate
import java.util.*

private const val NOTE_ID_KEY = "noteId"

/**
 * Экран добавления/редактирования заметки
 */
class AddEditNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddEditNoteBinding
    private var note: Note? = null

    private val noteDetailViewModel: NoteDetailViewModel by viewModels { ViewModelFactory(null) }

    private var hasScheduleDateChanged = false
    private val calendar = Calendar.getInstance()
    private val datePickerSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(year, month, day)
            hasScheduleDateChanged = true
            binding.openCalendar.text = Note.timeAsDate(calendar.time.time)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        note = getNoteFromArguments()
        setUpFormFields()

        with(requireContext() as AppCompatActivity) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        binding.title.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), FormAttributes.NOTE_TITLE), FormAttributes.NOTE_TITLE
            )
        }
        binding.content.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), FormAttributes.NOTE_CONTENT), FormAttributes.NOTE_CONTENT
            )
        }
        binding.addNote.setOnClickListener { saveNote() }
        binding.openCalendar.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    /**
     * Переход на экран списка заметок
     */
    private fun goToNotesList() {
        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .menu.findItem(R.id.notes_list).isChecked = true

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, NotesFragment())
            .commit()
    }

    /**
     * Получает заметку по переданному в аргументах id
     * @return заметку если id был передан и по этому id есть заметка в базе, иначе null
     */
    private fun getNoteFromArguments(): Note? {
        val noteId = arguments?.get(NOTE_ID_KEY) as UUID? ?: return null
        return noteDetailViewModel.getNote(noteId)
    }

    /**
     * Сохраняет заметку в хранилище.
     * Перед сохранением валидирует поля формы. В случае наличия ошибок сохранения не происходит
     */
    private fun saveNote() {
        val titleErrors = validate(binding.title.editableText.toString(), FormAttributes.NOTE_TITLE)
        val contentErrors = validate(binding.content.editableText.toString(), FormAttributes.NOTE_CONTENT)

        updateUiErrors(titleErrors, FormAttributes.NOTE_TITLE)
        updateUiErrors(contentErrors, FormAttributes.NOTE_CONTENT)

        if (!titleErrors.hasErrors() && !contentErrors.hasErrors()) {
            val isSuccessfully = noteDetailViewModel.saveNote(
                Note(
                    id = note?.id ?: UUID.randomUUID(),
                    title = binding.title.editableText.toString(),
                    content = binding.content.editableText.toString(),
                    userCreatorId = UserPreferences.get().userId,
                    createTime = note?.createTime ?: System.currentTimeMillis(),
                    scheduleTime = if (hasScheduleDateChanged) {
                        calendar.time.time
                    } else {
                        note?.scheduleTime
                    }
                )
            )

            if (isSuccessfully) {
                goToNotesList()
            } else {
                Toast.makeText(requireContext(), getString(R.string.failure_note_creation_message), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.fix_errors_message), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Выставляет полям формы начальные значения
     * На случай, когда текущий экран - экран редактирования
     */
    private fun setUpFormFields() {
        note?.let { note ->
            binding.title.setText(note.title)
            binding.content.setText(note.content)

            if (note.scheduleTime != null) {
                binding.openCalendar.text = Note.timeAsDate(note.scheduleTime)
            }
        }
    }

    /**
     * Обновляет UI, добавляя или убриая ошибки валидации
     * @param state результат валидации
     * @param attribute атрибут формы
     */
    private fun updateUiErrors(state: ValidationResult, attribute: FormAttributes) {
        if (state.hasErrors()) {
            val errors = state.getErrors().joinToString(separator = "\n")

            if (attribute == FormAttributes.NOTE_TITLE) {
                binding.title.setBackgroundResource(R.drawable.form_field_error_bg)
                binding.titleError.text = errors
                binding.titleError.visibility = View.VISIBLE
            } else {
                binding.content.setBackgroundResource(R.drawable.form_field_error_bg)
                binding.contentError.text = errors
                binding.contentError.visibility = View.VISIBLE
            }
        } else {
            if (attribute == FormAttributes.NOTE_TITLE) {
                binding.title.setBackgroundResource(R.drawable.form_field_bg)
                binding.titleError.visibility = View.GONE
            } else {
                binding.content.setBackgroundResource(R.drawable.form_field_bg)
                binding.contentError.visibility = View.GONE
            }
        }
    }

    companion object {
        fun newInstance(noteId: UUID?): AddEditNoteFragment {
            return AddEditNoteFragment().apply {
                arguments = bundleOf(
                    NOTE_ID_KEY to noteId,
                )
            }
        }
    }
}