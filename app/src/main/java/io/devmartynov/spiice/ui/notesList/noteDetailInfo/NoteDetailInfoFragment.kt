package io.devmartynov.spiice.ui.notesList.noteDetailInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentNoteDetailInfoBinding
import io.devmartynov.spiice.model.note.Note

/**
 * Детальная информация заметки
 */
class NoteDetailInfoFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNoteDetailInfoBinding

    companion object {
        const val TAG = "NoteDetailInfoFragment"
        const val NOTE_KEY = "note_key"

        fun newInstance(note: Note): NoteDetailInfoFragment {
            return NoteDetailInfoFragment().apply {
                arguments = bundleOf(
                    NOTE_KEY to note
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailInfoBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note = arguments?.get(NOTE_KEY) as? Note ?: return

        binding.noteTitle.text = note.title
        binding.noteContent.text = note.content
        binding.noteCreateDate.text = Note.timeAsDate(note.createTime)
        binding.noteScheduleDate.text = if (note.scheduleTime != null) {
            Note.timeAsDate(note.scheduleTime)
        } else {
            getString(R.string.note_no_schedule_date_label)
        }
    }
}