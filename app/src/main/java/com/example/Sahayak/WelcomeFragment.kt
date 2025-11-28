package com.example.sahayak

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // --- Find all the new UI elements ---
        val tvWelcomeMessage: TextView = view.findViewById(R.id.tv_welcome_message)
        val btnProfileIcon: ImageButton = view.findViewById(R.id.btn_profile_icon)

        val cardSos: CardView = view.findViewById(R.id.card_sos)
        val cardPension: CardView = view.findViewById(R.id.card_pension)
        val cardInsurance: CardView = view.findViewById(R.id.card_insurance)
        val cardEmergency: CardView = view.findViewById(R.id.card_emergency)
        val cardTalkie: CardView = view.findViewById(R.id.card_talkie)
        val cardFunzone: CardView = view.findViewById(R.id.card_funzone)

        // --- UPDATED: Get User's Name Correctly ---
        val prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)

        // 1. Get who is currently logged in
        val currentUser = prefs?.getString("CURRENT_USER", "") ?: ""

        // 2. Get the specific First Name associated with that user
        // We use the same key format we created in CreateProfileFragment: "FIRST_NAME_Sam"
        val firstName = prefs?.getString("FIRST_NAME_$currentUser", "User")

        tvWelcomeMessage.text = getString(R.string.welcome_greeting, firstName)
        // ------------------------------------------

        // --- Set OnClick Listeners ---

        // Listener for the Profile Icon (Top Right)
        btnProfileIcon.setOnClickListener {
            // Ensure you update ProfileFragment logic too if it displays data!
            (activity as MainActivity).showFragment(ProfileFragment())
        }

        // Feature Cards
        cardSos.setOnClickListener {
            dialNumber("100") // Call Police
        }

        cardPension.setOnClickListener {
            (activity as MainActivity).showFragment(PensionFragment())
        }

        cardInsurance.setOnClickListener {
            (activity as MainActivity).showFragment(InsuranceFragment())
        }

        cardEmergency.setOnClickListener {
            (activity as MainActivity).showFragment(EmergencyContactFragment())
        }

        cardTalkie.setOnClickListener {
            (activity as MainActivity).showFragment(TalkieFragment())
        }

        cardFunzone.setOnClickListener {
            (activity as MainActivity).showFragment(FunzoneFragment())
        }

        return view
    }

    private fun dialNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }
}