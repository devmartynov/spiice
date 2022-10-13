package io.devmartynov.spiice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        findViewById<Button>(R.id.sign_up).setOnClickListener(::sighUp)
        findViewById<Button>(R.id.log_in).setOnClickListener(::goToLogin)
    }

    private fun sighUp(view: View) {
        Toast.makeText(this, getString(R.string.sign_up_label), Toast.LENGTH_SHORT).show()
    }

    private fun goToLogin(view: View) {
        startActivity(
            Intent(this, LoginActivity::class.java)
        )
    }
}