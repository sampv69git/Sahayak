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
    lateinit var bottomNavigation: BottomNavigationView
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleHelper.setLocale(this)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        bottomNavigation = findViewById(R.id.bottom_navigation)

        // 1. Setup Navigation Selection
        bottomNavigation.setOnItemSelectedListener { item ->
            // I REMOVED the "if (item.isChecked)" check here.
            // This ensures that even if the system thinks it's already selected,
            // we still load the fragment when you click it.
            when (item.itemId) {
                R.id.nav_home -> { showFragment(WelcomeFragment()); true }
                R.id.nav_pension -> { showFragment(PensionFragment()); true }
                R.id.nav_insurance -> { showFragment(InsuranceFragment()); true }
                R.id.nav_emergency -> { showFragment(EmergencyContactFragment()); true }
                R.id.nav_talkie -> { showFragment(TalkieFragment()); true }
                else -> false
            }
        }

        // 2. BACK STACK LISTENER
        // Detects back button presses and updates the bottom highlight
        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment != null) {
                when (currentFragment) {
                    is WelcomeFragment -> bottomNavigation.menu.findItem(R.id.nav_home).isChecked = true
                    is PensionFragment -> bottomNavigation.menu.findItem(R.id.nav_pension).isChecked = true
                    is InsuranceFragment -> bottomNavigation.menu.findItem(R.id.nav_insurance).isChecked = true
                    is EmergencyContactFragment -> bottomNavigation.menu.findItem(R.id.nav_emergency).isChecked = true
                    is TalkieFragment -> bottomNavigation.menu.findItem(R.id.nav_talkie).isChecked = true
                }
            }
        }

        // 3. Initial Checks
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
                updateNavigationVisibility()

                // --- THE FIX IS HERE ---
                // We manually load the WelcomeFragment immediately.
                // We do this because the BottomNav might already be on "Home" by default,
                // so selecting it programmatically might not trigger the listener.
                showFragment(WelcomeFragment())

                // Ensure the button is visually selected
                bottomNavigation.selectedItemId = R.id.nav_home
            } else {
                showFragment(CreateProfileFragment())
            }
        }
    }

    // Helper to switch tabs programmatically
    fun switchToTab(tabId: Int) {
        bottomNavigation.selectedItemId = tabId
    }

    fun updateNavigationVisibility() {
        bottomNavigation.menu.findItem(R.id.nav_home).isVisible = true
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