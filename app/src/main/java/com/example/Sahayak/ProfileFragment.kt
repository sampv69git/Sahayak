package com.example.sahayak

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sahayak.LoginActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val prefs = requireContext().getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""

        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvAge = view.findViewById<TextView>(R.id.tvProfileAge)
        val tvSex = view.findViewById<TextView>(R.id.tvProfileSex)
        val tvHobby = view.findViewById<TextView>(R.id.tvProfileHobby)
        val btnEdit = view.findViewById<Button>(R.id.btnEditProfile)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // Load Data
        val fName = prefs.getString("${currentUser}_FIRST_NAME", "")
        val lName = prefs.getString("${currentUser}_LAST_NAME", "")
        val age = prefs.getString("${currentUser}_AGE", "")
        val hobby = prefs.getString("${currentUser}_HOBBY", "")

        tvName.text = "$fName $lName"
        tvAge.text = "${getString(R.string.age)}: $age"
        tvHobby.text = "${getString(R.string.hobby)}: $hobby"
        tvSex.text = getString(R.string.sex)

        // Navigate to Edit Profile
        btnEdit.setOnClickListener {
            activity?.let { act ->
                (act as MainActivity).showFragment(CreateProfileFragment())
            }
        }

        // Trigger Logout Confirmation
        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        return view
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_confirm_title))
            .setMessage(getString(R.string.logout_confirm_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                performLogout()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun performLogout() {
        val prefs = requireContext().getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)

        // --- CRITICAL FIX HERE ---
        val editor = prefs.edit()
        editor.remove("CURRENT_USER")   // Remove the name
        editor.remove("IS_LOGGED_IN")   // Remove the Login Flag (Stops the loop)
        editor.commit() // Commit immediately

        // Go to Login
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}