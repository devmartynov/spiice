package io.devmartynov.spiice.ui.addEditNote

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.utils.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.buildErrorCauseMessage
import io.devmartynov.spiice.utils.validation.ValidationResult
import io.devmartynov.spiice.databinding.FragmentAddEditNoteBinding
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.ui.shared.BannerView
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState
import io.devmartynov.spiice.utils.validation.validate
import java.util.*

/**
 * Экран добавления/редактирования заметки
 */
@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {
    private lateinit var binding: FragmentAddEditNoteBinding
    private val noteDetailViewModel: NoteDetailViewModel by viewModels()
    private var note: Note? = null
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

        note = arguments?.getSerializable("note") as Note?

        setUpFormFields()

        binding.banner.setCloseClick { binding.banner.hide() }

        noteDetailViewModel.savingState.observe(viewLifecycleOwner) { asyncOperationState ->
            when (asyncOperationState) {
                is AsyncOperationState.Loading -> {
                }
                is AsyncOperationState.Success -> {
                    binding.banner.setMode(BannerView.BannerMode.SUCCESS)
                    binding.banner.setText(getString(R.string.note_create_success_message))
                    binding.banner.show()
                    clearFields()
                }
                is AsyncOperationState.Failure -> {
                    binding.banner.setMode(BannerView.BannerMode.WARNING)
                    binding.banner.setText(
                        buildErrorCauseMessage(
                            getString(R.string.failure_note_creation_message),
                            asyncOperationState.error
                        )
                    )
                    binding.banner.show()
                }
                is AsyncOperationState.Idle -> {
                }
            }
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
     * Очищает поля ввода
     */
    private fun clearFields() {
        binding.title.editableText.clear()
        binding.content.editableText.clear()
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
            noteDetailViewModel.saveNote(
                Note(
                    id = note?.id ?: UUID.randomUUID(),
                    title = binding.title.editableText.toString(),
                    content = binding.content.editableText.toString(),
                    userCreatorId = noteDetailViewModel.userId,
                    createTime = note?.createTime ?: System.currentTimeMillis(),
                    scheduleTime = if (hasScheduleDateChanged) {
                        calendar.time.time
                    } else {
                        note?.scheduleTime
                    }
                )
            )
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
}