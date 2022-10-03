package io.devmartynov.spiice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LevelUpFeatureActivity : AppCompatActivity() {
    private val timer: Timer = TransitionTimer.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_up_feature)
        supportActionBar?.hide()
        timer.start(::goToNextScreen)
        findViewById<Button>(R.id.skip).setOnClickListener(::onSkipClick)
    }

    override fun onRestart() {
        super.onRestart()
        timer.start(::goToNextScreen)
    }

    private fun onSkipClick(view: View) {
        timer.stop()
        goToSignUp()
    }

    private fun goToNextScreen() {
        startActivity(Intent(this, EnjoyFeatureActivity::class.java))
    }

    private fun goToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}