package io.devmartynov.spiice.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentGreetingsBinding
import io.devmartynov.spiice.ui.auth.LoginFragment
import io.devmartynov.spiice.ui.feature.ProjectsFeatureFragment

private const val GREETINGS_FRAGMENT_TAG = "GREETINGS_FRAGMENT_TAG"

/**
 * Приветственный экран приложения в случае, если пользователь не авторизован.
 * @todo реализовать логику авторизации и смены стартового экрана
 */
class GreetingsFragment : Fragment() {
    private lateinit var binding: FragmentGreetingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGreetingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        binding.discoverPlatform.setOnClickListener { onDiscoverClick() }
        binding.logIn.setOnClickListener { onLoginClick() }
    }

    /**
     * Обработчик нажатия на кнопку изучения платформы
     */
    private fun onDiscoverClick() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ProjectsFeatureFragment())
            .addToBackStack(GREETINGS_FRAGMENT_TAG)
            .commit()
    }

    /**
     * Обработчик нажатия на кнопку входа
     */
    private fun onLoginClick() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }
}