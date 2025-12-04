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

        // 1. Get User Name
        val prefs = requireContext().getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""
        val firstName = prefs.getString("${currentUser}_FIRST_NAME", "User")

        // 2. Setup Header (IDs now match the XML I gave you)
        val tvWelcome = view.findViewById<TextView>(R.id.tv_welcome_message)
        val btnProfile = view.findViewById<ImageView>(R.id.btn_profile_icon)

        // Use the string resource with placeholder for the name
        tvWelcome.text = getString(R.string.welcome_greeting, firstName)

        // 3. Profile Click
        btnProfile.setOnClickListener {
            (activity as MainActivity).showFragment(ProfileFragment())
        }

        // 4. Set up Card Listeners (IDs now match the XML I gave you)

        // SOS -> Call 100
        view.findViewById<CardView>(R.id.card_sos).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:100")
            startActivity(intent)
        }

        // Pension
        view.findViewById<CardView>(R.id.card_pension).setOnClickListener {
            (activity as MainActivity).showFragment(PensionFragment())
        }

        // Insurance
        view.findViewById<CardView>(R.id.card_insurance).setOnClickListener {
            (activity as MainActivity).showFragment(InsuranceFragment())
        }

        // Emergency Contacts
        view.findViewById<CardView>(R.id.card_emergency).setOnClickListener {
            (activity as MainActivity).showFragment(EmergencyContactFragment())
        }

        // Talkie
        view.findViewById<CardView>(R.id.card_talkie).setOnClickListener {
            (activity as MainActivity).showFragment(TalkieFragment())
        }

        // Funzone
        view.findViewById<CardView>(R.id.card_funzone).setOnClickListener {
            (activity as MainActivity).showFragment(FunzoneFragment())
        }

        return view
    }
}