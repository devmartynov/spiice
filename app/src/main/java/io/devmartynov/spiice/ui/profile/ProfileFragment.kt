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

/**
 * Экран профиля
 */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

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

        binding.userFullName.text = viewModel.getUserFullName()
        binding.notesCount.text = String.format(
            getString(R.string.notes_count),
            viewModel.getNotesCount()
        )
        binding.deleteProfile.setOnClickListener {
            viewModel.deleteProfile()
            goToSignIn()
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
}