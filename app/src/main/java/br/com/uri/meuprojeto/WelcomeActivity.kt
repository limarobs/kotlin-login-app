package br.com.uri.meuprojeto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            navigateToLogin()
            return
        }

        val userEmailText = findViewById<TextView>(R.id.tvUserEmail)
        val logoutButton = findViewById<Button>(R.id.btnLogout)

        userEmailText.text = currentUser.email ?: "E-mail não disponível"

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
