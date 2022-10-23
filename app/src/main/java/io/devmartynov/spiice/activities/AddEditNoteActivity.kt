package io.devmartynov.spiice.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import io.devmartynov.spiice.FormAttributes
import io.devmartynov.spiice.view.NoteDetailViewModel
import io.devmartynov.spiice.R
import io.devmartynov.spiice.ValidationResult
import io.devmartynov.spiice.databinding.ActivityAddEditNoteBinding
import io.devmartynov.spiice.model.Note
import io.devmartynov.spiice.validate
import java.util.UUID

const val EXTRA_NOTE_ID = "EXTRA_NOTE_ID"

/**
 * Экран добавления/редактирования заметки
 */
class AddEditNoteActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddEditNoteBinding
    private var noteId: UUID? = null

    private val noteDetailViewModel: NoteDetailViewModel by lazy {
        ViewModelProvider(this)[NoteDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteId = intent.getSerializableExtra(EXTRA_NOTE_ID) as UUID?

        setUpFormFields()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.back.setOnClickListener { onBackPressed() }
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
            val newNoteId = noteId ?: UUID.randomUUID()
            val isSuccessfully = noteDetailViewModel.saveNote(
                Note(
                    id = newNoteId,
                    title = binding.title.editableText.toString(),
                    content = binding.content.editableText.toString(),
                )
            )

            if (isSuccessfully) {
                onBackPressed()
            } else {
                Toast.makeText(this, getString(R.string.failure_note_creation_message), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, getString(R.string.fix_errors_message), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Выставляет полям формы начальные значения
     * На случай, когда текущий экран - экран редактирования
     */
    private fun setUpFormFields() {
        if (noteId == null) {
            return
        }

        val note = noteDetailViewModel.getNote(noteId!!) ?: return

        binding.title.setText(note.title)
        binding.content.setText(note.content)
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