package com.example.sahayak

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply language
        LocaleHelper.setLocale(this)

        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (savedInstanceState == null) {
            // --- UPDATED PART ---
            // 1. Get the current user's name
            val currentUser = prefs.getString("CURRENT_USER", "")

            // 2. Check if THIS specific user has completed their profile
            val isProfileComplete = prefs.getBoolean("PROFILE_COMPLETE_$currentUser", false)

            if (isProfileComplete) {
                // If yes, go straight to the dashboard
                showFragment(WelcomeFragment())
            } else {
                // If no, force user to create a profile
                showFragment(CreateProfileFragment())
            }
            // --------------------
        }
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Keep back stack behavior if desired, or remove if you want to block going back
            .commit()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}