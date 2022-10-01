package io.devmartynov.spiice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class GreetingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greetings)
        findViewById<Button>(R.id.discover_platform).setOnClickListener(::onDiscoverClick)
    }

    private fun onDiscoverClick(view: View) {
        startActivity(
            Intent(this, ProjectsFeatureActivity::class.java)
        )
    }
}