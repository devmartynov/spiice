package io.devmartynov.spiice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class GreetingsActivity: AppCompatActivity() {
    private val timer: Timer = TransitionTimer.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greetings)
        findViewById<Button>(R.id.discover_platform).setOnClickListener(::onDiscoverClick)
        findViewById<Button>(R.id.log_in).setOnClickListener(::onLoginClick)
    }

    private fun onDiscoverClick(view: View) {
        startActivity(
            Intent(this , ProjectsFeatureActivity::class.java)
        )
    }

    override fun onRestart() {
        super.onRestart()
        timer.stop()
    }

    private fun onLoginClick(view: View) {

    }
}