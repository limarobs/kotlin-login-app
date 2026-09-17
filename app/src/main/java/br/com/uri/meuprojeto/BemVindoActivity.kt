package br.com.uri.meuprojeto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class BemVindoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bem_vindo)

        val usuario = FirebaseAuth.getInstance().currentUser

        if (usuario == null) {
            voltarParaLogin()
            return
        }

        val emailUsuario = findViewById<TextView>(R.id.tvEmailUsuario)
        val btnSair = findViewById<Button>(R.id.btnSair)

        emailUsuario.text = usuario.email ?: "E-mail não disponível"

        btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            voltarParaLogin()
        }
    }

    private fun voltarParaLogin() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
