package br.com.uri.meuprojeto

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val email = findViewById<EditText>(R.id.email)
        val senha = findViewById<EditText>(R.id.senha)

        val btnCadastro = findViewById<Button>(R.id.btnCadastro)
        val btnVoltar = findViewById<Button>(R.id.btnVoltar)

        btnCadastro.setOnClickListener {

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(
                    email.text.toString(),
                    senha.text.toString()
                )
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        Toast.makeText(this, "Cadastro OK!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Erro no cadastro", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }
}
