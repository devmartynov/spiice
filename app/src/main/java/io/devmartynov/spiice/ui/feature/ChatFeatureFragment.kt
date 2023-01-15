package io.devmartynov.spiice.ui.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.databinding.FragmentChatFeatureBinding
import io.devmartynov.spiice.utils.timer.TransitionTimer

/**
 * Экран онбординга (фича чатов - 3/5)
 */
@AndroidEntryPoint
class ChatFeatureFragment : Fragment() {
    private val viewModel: OnboardingViewModel by viewModels()
    private val timer = TransitionTimer.get()
    private lateinit var binding: FragmentChatFeatureBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatFeatureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.skip.setOnClickListener { onSkipClick() }
    }

    override fun onResume() {
        super.onResume()
        timer.start {
            requireActivity().runOnUiThread {
                findNavController().navigate(
                    ChatFeatureFragmentDirections
                        .actionChatFeatureFragmentToLevelUpFeatureFragment()
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        timer.stop()
    }

    /**
     * Обработчик нажатия на кнопку пропустить
     */
    private fun onSkipClick() {
        timer.stop()
        viewModel.setHasOnboardingFinish(hasFinished = true)
        findNavController().navigate(
            ChatFeatureFragmentDirections.actionChatFeatureFragmentToSignUpFragment()
        )
    }
}