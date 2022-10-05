package io.devmartynov.spiice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EnjoyFeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enjoy_feature)
        findViewById<Button>(R.id.skip).setOnClickListener(::onSkipClick)
    }

    private fun onSkipClick(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}