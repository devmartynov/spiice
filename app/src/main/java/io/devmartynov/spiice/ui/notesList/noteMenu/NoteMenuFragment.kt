package io.devmartynov.spiice.ui.notesList.noteMenu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNoteMenuBinding
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.utils.Callback

/**
 * Меню заметки с действиями над ней
 */
class NoteMenuFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNoteMenuBinding
    private var note: Note? = null
    private var safeDeleteNote: Callback? = null

    companion object {
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

        note = arguments?.getSerializable("note") as Note
        safeDeleteNote = arguments?.getSerializable("safeDeleteNote") as Callback

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
        val action = NoteMenuFragmentDirections
            .actionNoteMenuFragmentToAddEditNoteFragment(note = note )
        findNavController().navigate(action)
    }

    /**
     * Проверяет наличие всех необходимых данных для рыботы фрагмента
     */
    private fun checkArguments() {
        checkNotNull(note) { "You must pass note to fragment" }
        checkNotNull(safeDeleteNote) { "You must pass safeDeleteNote callback to fragment" }
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