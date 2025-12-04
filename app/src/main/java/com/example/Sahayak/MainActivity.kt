package com.example.sahayak

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var bottomNavigation: BottomNavigationView
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.setLocale(this)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        bottomNavigation = findViewById(R.id.bottom_navigation)

        // 1. Setup Navigation Selection
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_pension -> { showFragment(PensionFragment()); true }
                R.id.nav_insurance -> { showFragment(InsuranceFragment()); true }
                R.id.nav_emergency -> { showFragment(EmergencyContactFragment()); true }
                R.id.nav_talkie -> { showFragment(TalkieFragment()); true }
                else -> false
            }
        }

        // 2. Initial Checks
        if (savedInstanceState == null) {
            val currentUser = prefs.getString("CURRENT_USER", "") ?: ""

            if (currentUser.isEmpty()) {
                prefs.edit().putBoolean("IS_LOGGED_IN", false).apply()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return
            }

            val isProfileComplete = prefs.getBoolean("PROFILE_COMPLETE_$currentUser", false)

            if (isProfileComplete) {
                // Ensure buttons are visible when app starts
                updateNavigationVisibility()
                showFragment(WelcomeFragment())
            } else {
                showFragment(CreateProfileFragment())
            }
        }
    }

    // --- CRITICAL FIX: FORCE ALL BUTTONS TO BE VISIBLE ---
    // Even if other fragments call this, it will now SHOW everything instead of hiding it.
    fun updateNavigationVisibility() {
        bottomNavigation.menu.findItem(R.id.nav_pension).isVisible = true
        bottomNavigation.menu.findItem(R.id.nav_insurance).isVisible = true
        bottomNavigation.menu.findItem(R.id.nav_emergency).isVisible = true
        bottomNavigation.menu.findItem(R.id.nav_talkie).isVisible = true
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
}