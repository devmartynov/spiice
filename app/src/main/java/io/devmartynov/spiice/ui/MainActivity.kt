package io.devmartynov.spiice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.R
import io.devmartynov.spiice.repository.user.UserPreferencesRepository
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNav()
        startMainNavListener()
    }

    private fun setUpNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(getStartDestination())

        navController.setGraph(graph, intent.extras)
        bottomNavView = findViewById(R.id.bottom_navigation)
        bottomNavView.setupWithNavController(navController)
    }

    /**
     * Навешивает слушать выбора пункта меню в навигации
     */
    private fun startMainNavListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.notesFragment -> {
                    bottomNavView.visibility = View.VISIBLE
                }
                R.id.log_in or R.id.sign_up -> {
                    bottomNavView.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Определяет первый экран приложения
     */
    private fun getStartDestination(): Int {
        var startDestinationId = R.id.notesFragment

        if (!userPreferencesRepository.isAuthorized()) {
            startDestinationId = if (!userPreferencesRepository.hasOnboardingFinished) {
                R.id.greetingsFragment
            } else {
                R.id.loginFragment
            }
        }

        return startDestinationId
    }
}
