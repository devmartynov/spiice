package io.devmartynov.spiice.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentProfileBinding
import io.devmartynov.spiice.ui.ViewModelFactory
import io.devmartynov.spiice.ui.auth.LoginFragment
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState

/**
 * Экран профиля
 */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels { ViewModelFactory(null) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gettingNotesCountState.observe(viewLifecycleOwner) { gettingCountState ->
            when (gettingCountState) {
                is AsyncOperationState.Loading -> {

                }
                is AsyncOperationState.Success -> {
                    setNotesCount(gettingCountState.data as Long)
                }
                is AsyncOperationState.Failure -> {
                    setNotesCount(0L)
                }
                is AsyncOperationState.Idle -> {

                }
            }
        }
        viewModel.deletingProfileState.observe(viewLifecycleOwner) { deletingState ->
            when (deletingState) {
                is AsyncOperationState.Loading -> {
                }
                is AsyncOperationState.Success -> {
                    goToSignIn()
                }
                is AsyncOperationState.Failure -> {
                }
                is AsyncOperationState.Idle -> {
                }
            }
        }
        viewModel.getNotesCount()
        binding.userFullName.text = viewModel.getUserFullName()
        binding.deleteProfile.setOnClickListener {
            viewModel.deleteProfile()
        }
        binding.deleteAllNotes.setOnClickListener {
            viewModel.deleteAllNotes()
        }
        binding.signOut.setOnClickListener {
            viewModel.signOut()
            goToSignIn()
        }
    }

    /**
     * Переход на экран входа
     */
    private fun goToSignIn() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }

    private fun setNotesCount(count: Long) {
        binding.notesCount.text = String.format(getString(R.string.notes_count), count)
    }
}