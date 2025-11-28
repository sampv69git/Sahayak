package com.example.sahayak

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply language
        LocaleHelper.setLocale(this)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Check if user is already logged in
        val isLoggedIn = prefs.getBoolean("IS_LOGGED_IN", false)
        if (isLoggedIn) {
            goToMainActivity()
            return
        }

        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnRegister = findViewById<TextView>(R.id.btn_register)
        val tvChangeLanguage = findViewById<TextView>(R.id.tv_change_language)

        // Login button
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            val savedPassword = prefs.getString("USER_$username", null)

            if (username.isNotEmpty() && password.isNotEmpty() && password == savedPassword) {
                // --- UPDATED PART ---
                val editor = prefs.edit()
                editor.putBoolean("IS_LOGGED_IN", true)
                editor.putString("CURRENT_USER", username) // Saving who is currently logged in
                editor.apply()
                // --------------------

                goToMainActivity()
            } else {
                Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show()
            }
        }

        // Register button
        btnRegister.setOnClickListener {
            showRegisterDialog()
        }

        // Language button
        tvChangeLanguage.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showRegisterDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_register, null)

        val etNewUsername = dialogView.findViewById<EditText>(R.id.etNewUsername)
        val etNewPassword = dialogView.findViewById<EditText>(R.id.etNewPassword)

        builder.setView(dialogView)
            .setTitle(getString(R.string.register_new_user))
            .setPositiveButton(getString(R.string.register)) { dialog, id ->
                val username = etNewUsername.text.toString()
                val password = etNewPassword.text.toString()

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, getString(R.string.please_fill_all_fields), Toast.LENGTH_LONG).show()
                } else {
                    with(prefs.edit()) {
                        putString("USER_$username", password)
                        apply()
                    }
                    Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, id ->
                dialog.cancel()
            }

        builder.create().show()
    }

    private fun showLanguageDialog() {
        val languages = resources.getStringArray(R.array.languages)
        val languageCodes = resources.getStringArray(R.array.language_codes)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_language))
        builder.setItems(languages) { dialog, which ->
            val langCode = languageCodes[which]
            LocaleHelper.saveLocale(this, langCode)
            recreate()
        }
        builder.show()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}