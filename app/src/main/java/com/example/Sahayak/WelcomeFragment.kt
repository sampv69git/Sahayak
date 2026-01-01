package com.example.sahayak

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class WelcomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        val prefs = requireContext().getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""
        val firstName = prefs.getString("${currentUser}_FIRST_NAME", "User")

        val tvWelcome = view.findViewById<TextView>(R.id.tv_welcome_message)
        val btnProfile = view.findViewById<ImageView>(R.id.btn_profile_icon)

        tvWelcome.text = getString(R.string.welcome_greeting, firstName)

        btnProfile.setOnClickListener {
            (activity as MainActivity).showFragment(ProfileFragment())
        }

        // --- UPDATED LISTENERS (The Fix) ---

        // 1. SOS (Still calls 100 directly)
        view.findViewById<CardView>(R.id.card_sos).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:100")
            startActivity(intent)
        }

        // 2. Pension -> Highlight Bottom Pension Tab
        view.findViewById<CardView>(R.id.card_pension).setOnClickListener {
            (activity as MainActivity).switchToTab(R.id.nav_pension)
        }

        // 3. Insurance -> Highlight Bottom Insurance Tab
        view.findViewById<CardView>(R.id.card_insurance).setOnClickListener {
            (activity as MainActivity).switchToTab(R.id.nav_insurance)
        }

        // 4. Emergency -> Highlight Bottom Emergency Tab
        view.findViewById<CardView>(R.id.card_emergency).setOnClickListener {
            (activity as MainActivity).switchToTab(R.id.nav_emergency)
        }

        // 5. Talkie -> Highlight Bottom Talkie Tab
        view.findViewById<CardView>(R.id.card_talkie).setOnClickListener {
            (activity as MainActivity).switchToTab(R.id.nav_talkie)
        }

        // 6. Funzone (No bottom tab for this, so just show screen)
        view.findViewById<CardView>(R.id.card_funzone).setOnClickListener {
            (activity as MainActivity).showFragment(FunzoneFragment())
        }

        return view
    }
}