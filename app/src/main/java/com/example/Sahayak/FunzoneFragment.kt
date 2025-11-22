package com.example.sahayak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class FunzoneFragment : Fragment() {

    private val board = Array(3) { IntArray(3) { 0 } } // 0=Empty, 1=Player X, 2=Player O
    private var currentPlayer = 1 // 1 for X, 2 for O
    private lateinit var buttons: Array<Array<Button>>
    private lateinit var tvStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_funzone, container, false)
        tvStatus = view.findViewById(R.id.tvGameStatus)
        initializeButtons(view)
        resetGame()
        return view
    }

    private fun initializeButtons(view: View) {
        buttons = Array(3) { i ->
            Array(3) { j ->
                val id = resources.getIdentifier("button_$i$j", "id", requireActivity().packageName)
                val button: Button = view.findViewById(id)
                button.setOnClickListener { onCellClicked(i, j, button) }
                button
            }
        }
    }

    private fun onCellClicked(row: Int, col: Int, button: Button) {
        if (board[row][col] != 0) return // Cell already taken

        board[row][col] = currentPlayer
        button.text = if (currentPlayer == 1) "X" else "O"

        // --- FIX IS HERE ---
        // Previously, X was R.color.purple_500 (which was invisible on the purple button).
        // Now, BOTH X and O use R.color.teal_700 (Green) so they are clearly visible.
        button.setTextColor(resources.getColor(R.color.teal_700, null))
        // -------------------

        if (checkForWin()) {
            val winner = if (currentPlayer == 1) "X" else "O"
            showGameOverDialog(getString(R.string.game_over_win, winner))
            return
        }

        if (isBoardFull()) {
            showGameOverDialog(getString(R.string.game_over_draw))
            return
        }

        // Switch player
        currentPlayer = 3 - currentPlayer
        updateStatus()
    }

    private fun checkForWin(): Boolean {
        // Check rows, columns, and diagonals
        for (i in 0..2) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) return true // Row
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) return true // Column
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) return true // Main Diagonal
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) return true // Anti-Diagonal
        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it != 0 } }
    }

    private fun updateStatus() {
        val player = if (currentPlayer == 1) "X" else "O"
        tvStatus.text = getString(R.string.game_status_turn, player)
    }

    private fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = 0
                buttons[i][j].text = ""
            }
        }
        currentPlayer = 1
        updateStatus()
    }

    private fun showGameOverDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.game_restart)) { dialog, which ->
                resetGame()
            }
            .setCancelable(false)
            .show()
    }
}