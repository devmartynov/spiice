package io.devmartynov.spiice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.R
import io.devmartynov.spiice.repository.user.UserPreferencesRepository
import io.devmartynov.spiice.ui.addEditNote.AddEditNoteFragment
import io.devmartynov.spiice.ui.notesList.NotesFragment
import io.devmartynov.spiice.ui.profile.ProfileFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goToStartScreen()
        startMainNavListener()
    }

    /**
     * Определяет стартовый экран и переходит на него
     */
    private fun goToStartScreen() {
        val fragment = if (userPreferencesRepository.isAuthorized()) {
            NotesFragment()
        } else {
            GreetingsFragment()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container, fragment
            )
            .commit()
    }

    /**
     * Навешивает слушать выбора пункта меню в навигации
     */
    private fun startMainNavListener() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            var nextFragment: Fragment = NotesFragment()

            when (item.itemId) {
                R.id.notes_list -> {
                    nextFragment = NotesFragment()
                }
                R.id.notes_create -> {
                    nextFragment = AddEditNoteFragment()
                }
                R.id.profile -> {
                    nextFragment = ProfileFragment()
                }
            }

            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container, nextFragment
                )
                .commit()

            true
        }
    }
}