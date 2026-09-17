package br.com.uri.meuprojeto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = findViewById<EditText>(R.id.email)
        val senha = findViewById<EditText>(R.id.senha)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCadastro = findViewById<Button>(R.id.btnCadastro)

        // LOGIN
        btnLogin.setOnClickListener {

            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                    email.text.toString(),
                    senha.text.toString()
                )
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Login OK!",
                            Toast.LENGTH_SHORT
                        ).show()
                        abrirTelaInicial()
                    } else {
                        Toast.makeText(
                            this,
                            "Erro no login",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // IR PARA CADASTRO
        btnCadastro.setOnClickListener {

            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) {
            abrirTelaInicial()
        }
    }

    private fun abrirTelaInicial() {
        startActivity(Intent(this, BemVindoActivity::class.java))
        finish()
    }
}
