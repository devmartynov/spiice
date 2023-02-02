package io.devmartynov.spiice.ui.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.R

/**
 * Экран онбординга (фича наслаждения - 5/5)
 */
@AndroidEntryPoint
class EnjoyFeatureFragment : Fragment() {
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_enjoy_feature, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.skip).setOnClickListener {
            viewModel.setHasOnboardingFinish(hasFinished = true)
            findNavController().navigate(
                EnjoyFeatureFragmentDirections.actionEnjoyFeatureFragmentToSignUpFragment()
            )
        }
    }
}