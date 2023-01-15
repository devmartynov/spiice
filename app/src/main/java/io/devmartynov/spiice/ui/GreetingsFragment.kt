package io.devmartynov.spiice.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.devmartynov.spiice.R
import io.devmartynov.spiice.databinding.FragmentGreetingsBinding

/**
 * Приветственный экран приложения в случае, если пользователь не авторизован.
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

        binding.discoverPlatform.setOnClickListener {
            findNavController().navigate(R.id.action_greetingsFragment_to_projectsFeatureFragment)
        }
        binding.logIn.setOnClickListener {
            findNavController().navigate(R.id.action_greetingsFragment_to_loginFragment)
        }
    }
}