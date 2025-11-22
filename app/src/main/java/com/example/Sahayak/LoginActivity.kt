package com.example.sahayak

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences // <-- Make sure this is imported
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    // Define SharedPreferences at the class level
    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Apply the saved language *before* setting the view
        LocaleHelper.setLocale(this)

        // --- THIS IS THE FIX (Part 1) ---
        // Get SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Check if user is already logged in
        val isLoggedIn = prefs.getBoolean("IS_LOGGED_IN", false)
        if (isLoggedIn) {
            // User is already logged in, skip LoginActivity
            goToMainActivity()
            return // Stop the rest of this function from running
        }
        // --- END OF FIX (Part 1) ---

        // If not logged in, show the login screen
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

                // --- THIS IS THE FIX (Part 2) ---
                // Save the login state
                prefs.edit().putBoolean("IS_LOGGED_IN", true).apply()
                // --- END OF FIX (Part 2) ---

                // On success, go to the main app
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

    // Helper function to go to MainActivity
    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close login screen
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
                    // Save the new user to SharedPreferences
                    with(prefs.edit()) {
                        putString("USER_$username", password) // Save password keyed by username
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
        // Get language names and codes from strings.xml
        val languages = resources.getStringArray(R.array.languages)
        val languageCodes = resources.getStringArray(R.array.language_codes)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_language))
        builder.setItems(languages) { dialog, which ->
            val langCode = languageCodes[which]
            LocaleHelper.saveLocale(this, langCode)

            // Recreate the app to apply the new language
            recreate()
        }
        builder.show()
    }

    // This is required to apply the language
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}