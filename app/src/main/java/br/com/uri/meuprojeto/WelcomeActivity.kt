package br.com.uri.meuprojeto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WelcomeActivity : AppCompatActivity() {

    private lateinit var teamOneMatchsticks: MatchstickScoreView
    private lateinit var teamTwoMatchsticks: MatchstickScoreView
    private lateinit var matchStatusText: TextView

    private var teamOneScore = 0
    private var teamTwoScore = 0
    private var winningScore = DEFAULT_WINNING_SCORE

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

        teamOneMatchsticks = findViewById(R.id.matchsticksTeamOne)
        teamTwoMatchsticks = findViewById(R.id.matchsticksTeamTwo)
        matchStatusText = findViewById(R.id.tvMatchStatus)

        teamOneScore = savedInstanceState?.getInt(TEAM_ONE_SCORE, 0) ?: 0
        teamTwoScore = savedInstanceState?.getInt(TEAM_TWO_SCORE, 0) ?: 0
        winningScore = savedInstanceState?.getInt(WINNING_SCORE, DEFAULT_WINNING_SCORE)
            ?: DEFAULT_WINNING_SCORE

        userEmailText.text = currentUser.email ?: "E-mail não disponível"

        findViewById<Button>(R.id.btnTeamOneIncrease).setOnClickListener {
            updateScore(teamOneScore + 1, teamTwoScore)
        }
        findViewById<Button>(R.id.btnTeamOneDecrease).setOnClickListener {
            updateScore((teamOneScore - 1).coerceAtLeast(0), teamTwoScore)
        }
        findViewById<Button>(R.id.btnTeamTwoIncrease).setOnClickListener {
            updateScore(teamOneScore, teamTwoScore + 1)
        }
        findViewById<Button>(R.id.btnTeamTwoDecrease).setOnClickListener {
            updateScore(teamOneScore, (teamTwoScore - 1).coerceAtLeast(0))
        }
        findViewById<Button>(R.id.btnMatchSettings).setOnClickListener {
            showMatchSettingsDialog()
        }
        updateScore(teamOneScore, teamTwoScore)

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

    private fun updateScore(newTeamOneScore: Int, newTeamTwoScore: Int) {
        teamOneScore = newTeamOneScore
        teamTwoScore = newTeamTwoScore

        teamOneMatchsticks.score = teamOneScore
        teamTwoMatchsticks.score = teamTwoScore

        matchStatusText.text = when {
            teamOneScore >= winningScore -> {
                showWinnerDialog("Nós vencemos a partida!")
                "Nós vencemos a partida!"
            }
            teamTwoScore >= winningScore -> {
                showWinnerDialog("Eles venceram a partida!")
                "Eles venceram a partida!"
            }
            else -> "Primeira equipe a $winningScore pontos vence"
        }
    }

    private fun showMatchSettingsDialog() {
        val matchOptions = arrayOf("Até 24 pontos", "Até 30 pontos")
        var selectedOption = if (winningScore == 24) 0 else 1

        MaterialAlertDialogBuilder(this)
            .setTitle("Configurar partida")
            .setSingleChoiceItems(matchOptions, selectedOption) { _, option ->
                selectedOption = option
            }
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Salvar") { _, _ ->
                winningScore = if (selectedOption == 0) 24 else 30
                updateScore(0, 0)
            }
            .show()
    }

    private fun showWinnerDialog(winnerMessage: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Fim de partida")
            .setMessage("$winnerMessage\n\nO placar será reiniciado.")
            .setPositiveButton("OK") { _, _ ->
                updateScore(0, 0)
            }
            .setCancelable(false)
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TEAM_ONE_SCORE, teamOneScore)
        outState.putInt(TEAM_TWO_SCORE, teamTwoScore)
        outState.putInt(WINNING_SCORE, winningScore)
    }

    companion object {
        private const val TEAM_ONE_SCORE = "team_one_score"
        private const val TEAM_TWO_SCORE = "team_two_score"
        private const val WINNING_SCORE = "winning_score"
        private const val DEFAULT_WINNING_SCORE = 30
    }
}
