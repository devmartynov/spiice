package io.devmartynov.spiice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.log_in).setOnClickListener(::login)
        findViewById<Button>(R.id.sign_up).setOnClickListener(::goToSignUp)
    }

    private fun login(view: View) {
        Toast.makeText(this, getString(R.string.log_in_label), Toast.LENGTH_SHORT).show()
    }

    private fun goToSignUp(view: View) {
        startActivity(
            Intent(this, SignUpActivity::class.java)
        )
    }
}