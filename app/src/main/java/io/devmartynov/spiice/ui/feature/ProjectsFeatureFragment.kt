package io.devmartynov.spiice.ui.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.databinding.FragmentProjectsFeatureBinding
import io.devmartynov.spiice.utils.timer.TransitionTimer

/**
 * Экран онбординга (фича проектов - 1/5)
 */
@AndroidEntryPoint
class ProjectsFeatureFragment : Fragment() {
    private val viewModel: OnboardingViewModel by viewModels()
    private val timer = TransitionTimer.get()

    private lateinit var binding: FragmentProjectsFeatureBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectsFeatureBinding.inflate(layoutInflater, container, false)
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
                    ProjectsFeatureFragmentDirections
                        .actionProjectsFeatureFragmentToMoneyFeatureFragment()
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
            ProjectsFeatureFragmentDirections.actionProjectsFeatureFragmentToSignUpFragment()
        )
    }
}