package io.devmartynov.spiice.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.devmartynov.spiice.R
import io.devmartynov.spiice.timer.Timer
import io.devmartynov.spiice.timer.TransitionTimer

class MoneyFeatureActivity : AppCompatActivity() {
    private val timer: Timer = TransitionTimer.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_feature)
        findViewById<Button>(R.id.skip).setOnClickListener(::onSkipClick)
    }

    override fun onResume() {
        super.onResume()
        timer.start(::goToNextScreen)
    }

    override fun onPause() {
        super.onPause()
        timer.stop()
    }

    private fun onSkipClick(view: View) {
        timer.stop()
        goToSignUp()
    }

    private fun goToNextScreen() {
        startActivity(Intent(this, ChatFeatureActivity::class.java))
    }

    private fun goToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}