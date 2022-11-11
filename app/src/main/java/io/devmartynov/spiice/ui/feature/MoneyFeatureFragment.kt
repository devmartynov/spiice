package io.devmartynov.spiice.ui.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentMoneyFeatureBinding
import io.devmartynov.spiice.utils.timer.TransitionTimer
import io.devmartynov.spiice.ui.auth.SignUpFragment

private const val MONEY_FRAGMENT_TAG = "MONEY_FRAGMENT_TAG"

/**
 * Экран фичи заработка (2/5)
 */
class MoneyFeatureFragment : Fragment() {
    private val timer = TransitionTimer.get()
    private lateinit var binding: FragmentMoneyFeatureBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoneyFeatureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.skip.setOnClickListener { onSkipClick() }
    }

    override fun onResume() {
        super.onResume()
        timer.start(::goToNextScreen)
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
        goToSignUp()
    }

    /**
     * Переход на следующий экран фичи
     */
    private fun goToNextScreen() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ChatFeatureFragment())
            .addToBackStack(MONEY_FRAGMENT_TAG)
            .commit()
    }

    /**
     * Переход на экран регистрации
     */
    private fun goToSignUp() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SignUpFragment())
            .commit()
    }
}