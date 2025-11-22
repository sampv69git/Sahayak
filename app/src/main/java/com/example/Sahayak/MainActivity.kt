package com.example.sahayak

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply the saved language
        LocaleHelper.setLocale(this)

        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if (savedInstanceState == null) {
            // Check if profile is already complete
            val isProfileComplete = prefs.getBoolean("PROFILE_COMPLETE", false)
            if (isProfileComplete) {
                // If yes, go straight to the dashboard
                showFragment(WelcomeFragment())
            } else {
                // If no, force user to create a profile
                showFragment(CreateProfileFragment())
            }
        }
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    // This is required to apply the language
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}