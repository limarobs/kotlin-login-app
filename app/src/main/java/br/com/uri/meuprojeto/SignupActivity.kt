package br.com.uri.meuprojeto

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)

        val signupButton = findViewById<Button>(R.id.btnSignup)
        val backButton = findViewById<Button>(R.id.btnBack)

        signupButton.setOnClickListener {
            val emailValue = emailField.text.toString().trim()
            val passwordValue = passwordField.text.toString()

            if (!hasValidInputs(emailField, passwordField, emailValue, passwordValue)) {
                return@setOnClickListener
            }

            signupButton.isEnabled = false

            auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener { task ->
                    signupButton.isEnabled = true

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Cadastro OK!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, WelcomeActivity::class.java))
                        finishAffinity()
                    } else {
                        Toast.makeText(
                            this,
                            task.exception?.localizedMessage ?: "Não foi possível criar a conta.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun hasValidInputs(
        emailField: EditText,
        passwordField: EditText,
        emailValue: String,
        passwordValue: String
    ): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            emailField.error = "Informe um e-mail válido."
            emailField.requestFocus()
            return false
        }

        if (passwordValue.length < 6) {
            passwordField.error = "A senha deve ter pelo menos 6 caracteres."
            passwordField.requestFocus()
            return false
        }

        return true
    }
}
