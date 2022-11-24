package io.devmartynov.spiice.ui.notesList.noteMenu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNoteMenuBinding
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.ui.addEditNote.AddEditNoteFragment
import io.devmartynov.spiice.ui.notesList.NOTES_FRAGMENT_TAG
import io.devmartynov.spiice.utils.timer.callback

/**
 * Меню заметки с действиями над ней
 */
class NoteMenuFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNoteMenuBinding
    var note: Note? = null
    var safeDeleteNote: callback? = null

    companion object {
        const val TAG = "NoteMenuFragment"
        private const val SHARE_CONTENT_TYPE = "text/plain"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkArguments()

        binding.share.setOnClickListener {
            shareNote()
            dismiss()
        }
        binding.edit.setOnClickListener {
            goToAddEditScreen()
            dismiss()
        }
        binding.delete.setOnClickListener {
            safeDeleteNote?.invoke()
            dismiss()
        }
    }

    /**
     * Переходит на экран редактирования заметки.
     */
    private fun goToAddEditScreen() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, AddEditNoteFragment().apply {
                this.note = this@NoteMenuFragment.note
            })
            .addToBackStack(NOTES_FRAGMENT_TAG)
            .commit()
    }

    /**
     * Проверяет наличие всех необходимых данных для рыботы фрагмента
     */
    private fun checkArguments() {
        if (note == null) {
            throw IllegalArgumentException("You must pass note to fragment")
        }
        if (safeDeleteNote == null) {
            throw IllegalArgumentException("You must pass safeDeleteNote callback to fragment")
        }
    }

    /**
     * Формирует интент для отправки текста заметки и запускает его.
     */
    private fun shareNote() {
        Intent(Intent.ACTION_SEND)
            .apply {
                type = SHARE_CONTENT_TYPE
                putExtra(Intent.EXTRA_TEXT, note!!.content)
                putExtra(Intent.EXTRA_SUBJECT, note!!.title)
            }
            .also { intent ->
                startActivity(
                    Intent.createChooser(intent, getString(R.string.share_dialog_title))
                )
            }
    }
}