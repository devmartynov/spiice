package io.devmartynov.spiice.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.devmartynov.spiice.R

class EnjoyFeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enjoy_feature)
        findViewById<Button>(R.id.skip).setOnClickListener { onSkipClick() }
    }

    private fun onSkipClick() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}