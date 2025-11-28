package com.example.sahayak

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvName: TextView = view.findViewById(R.id.tvProfileName)
        val tvAge: TextView = view.findViewById(R.id.tvProfileAge)
        val tvSex: TextView = view.findViewById(R.id.tvProfileSex)
        val tvHobby: TextView = view.findViewById(R.id.tvProfileHobby)
        val tvMarital: TextView = view.findViewById(R.id.tvProfileMarital)

        val btnEditProfile: Button = view.findViewById(R.id.btnEditProfile)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)

        // Load data
        val prefs: SharedPreferences = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)!!

        // --- UPDATED LOGIC: Get Current User First ---
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""

        // Fetch data specifically for this user
        val firstName = prefs.getString("FIRST_NAME_$currentUser", "")
        val lastName = prefs.getString("LAST_NAME_$currentUser", "")
        val age = prefs.getString("AGE_$currentUser", "N/A")
        val sex = prefs.getString("SEX_$currentUser", "N/A")
        val hobby = prefs.getString("HOBBY_$currentUser", "N/A")
        val maritalStatus = prefs.getString("MARITAL_STATUS_$currentUser", "N/A")
        // ---------------------------------------------

        tvName.text = "$firstName $lastName"
        tvAge.text = "${getString(R.string.age)}: $age"
        tvSex.text = "${getString(R.string.sex)} $sex"
        tvHobby.text = "${getString(R.string.hobby)}: $hobby"
        tvMarital.text = "${getString(R.string.marital_status)}: $maritalStatus"

        // Edit Profile Button
        btnEditProfile.setOnClickListener {
            (activity as MainActivity).showFragment(CreateProfileFragment())
        }

        // Logout Button
        btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_confirm_title))
                .setMessage(getString(R.string.logout_confirm_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->

                    // 1. Set the logged in flag to false
                    prefs.edit().putBoolean("IS_LOGGED_IN", false).apply()

                    // 2. Go back to the LoginActivity
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish() // Close the MainActivity
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
        return view
    }
}