package io.devmartynov.spiice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.devmartynov.spiice.R
import io.devmartynov.spiice.model.user.UserPreferences
import io.devmartynov.spiice.ui.notesList.NotesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = if (UserPreferences.get().isAuthorized()) {
            NotesFragment()
        } else {
            GreetingsFragment()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container, fragment)
            .commit()
    }
}