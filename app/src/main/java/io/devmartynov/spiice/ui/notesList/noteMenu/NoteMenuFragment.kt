package io.devmartynov.spiice.ui.notesList.noteMenu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNoteMenuBinding
import io.devmartynov.spiice.model.Note
import io.devmartynov.spiice.ui.notesList.NotesViewModel
import java.io.Serializable
import java.util.*

/**
 * Меню заметки с действиями над ней
 */
class NoteMenuFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNoteMenuBinding
    private val viewModel: NotesViewModel by activityViewModels()
    private var note: Note? = null
    private var callbacks: Callbacks? = null

    companion object {
        const val TAG = "NoteMenuFragment"
        private const val NOTE_ID_KEY = "note_id_key"
        private const val CALLBACKS_KEY = "callbacks_key"
        private const val SHARE_CONTENT_TYPE = "text/plain"

        fun newInstance(noteId: UUID, callbacks: Callbacks): NoteMenuFragment {
            return NoteMenuFragment().apply {
                arguments = bundleOf(
                    NOTE_ID_KEY to noteId,
                    CALLBACKS_KEY to callbacks
                )
            }
        }
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

        callbacks = arguments?.get(CALLBACKS_KEY) as? Callbacks
            ?: throw IllegalArgumentException("You must pass callbacks to this fragment")
        val noteId = arguments?.get(NOTE_ID_KEY) as? UUID
            ?: throw IllegalArgumentException("You must pass note id to this fragment")
        note = viewModel.getNote(noteId)
            ?: throw IllegalArgumentException("No note was found by passed id: $noteId")

        binding.share.setOnClickListener {
            shareNote()
            dismiss()
        }
        binding.edit.setOnClickListener {
            callbacks?.goToEditScreen()
            dismiss()
        }
        binding.delete.setOnClickListener {
            callbacks?.safeDeleteNote()
            dismiss()
        }

        // смена цвета иконки и установка новой в кнопку
        val icDelete = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_delete)
        val wrappedIcDelete = DrawableCompat.wrap(icDelete!!)
        DrawableCompat.setTint(wrappedIcDelete, requireContext().getColor(R.color.main))
        binding.delete.setCompoundDrawablesWithIntrinsicBounds(wrappedIcDelete, null, null, null);
    }

    /**
     * Формирует интент для отправки текста заметки и запускает его.
     */
    private fun shareNote() {
        Intent(Intent.ACTION_SEND)
            .apply {
                type = SHARE_CONTENT_TYPE
                putExtra(Intent.EXTRA_TEXT, viewModel.getNoteSharingInfo(note!!))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            }
            .also { intent ->
                startActivity(
                    Intent.createChooser(intent, getString(R.string.share_dialog_title))
                )
            }
    }

    /**
     * Колбеки для экшенов.
     * По сути это костыль.
     */
    interface Callbacks : Serializable {
        fun goToEditScreen()
        fun safeDeleteNote()
    }
}