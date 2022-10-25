package io.devmartynov.spiice.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import io.devmartynov.spiice.R

class GreetingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greetings)
        findViewById<Button>(R.id.discover_platform).setOnClickListener { onDiscoverClick() }
        findViewById<Button>(R.id.log_in).setOnClickListener { onLoginClick() }
    }

    private fun onDiscoverClick() {
        startActivity(
            Intent(this , ProjectsFeatureActivity::class.java)
        )
    }

    private fun onLoginClick() {
        startActivity(
            Intent(this , LoginActivity::class.java)
        )
    }
}