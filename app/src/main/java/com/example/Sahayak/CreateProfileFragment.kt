package com.example.sahayak

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class CreateProfileFragment : Fragment() {

    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_create_profile, container, false)

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Initialize Views (MATCHING YOUR XML IDs)
        val etFirstName = rootView.findViewById<EditText>(R.id.etFirstName)
        val etLastName = rootView.findViewById<EditText>(R.id.etLastName)
        val etAge = rootView.findViewById<EditText>(R.id.etAge)
        val etHobby = rootView.findViewById<EditText>(R.id.etHobby)

        // Pension Container & Amount
        val layoutPensionDetails = rootView.findViewById<LinearLayout>(R.id.layoutPensionDetails)
        val etPensionAmount = rootView.findViewById<EditText>(R.id.etPensionAmount)

        // Insurance Container & Details (THE MISSING PART RESTORED)
        val layoutInsuranceDetails = rootView.findViewById<LinearLayout>(R.id.layoutInsuranceDetails)
        val etInsuranceCompany = rootView.findViewById<EditText>(R.id.etInsuranceCompany)
        val etInsurancePlan = rootView.findViewById<EditText>(R.id.etInsurancePlan)
        val etInsurancePremium = rootView.findViewById<EditText>(R.id.etInsurancePremium)

        // Radio Groups
        val rgSex = rootView.findViewById<RadioGroup>(R.id.rgSex)
        val rbMale = rootView.findViewById<RadioButton>(R.id.rbMale)
        val rbFemale = rootView.findViewById<RadioButton>(R.id.rbFemale)

        val rgMarital = rootView.findViewById<RadioGroup>(R.id.rgMaritalStatus)
        val rbSingle = rootView.findViewById<RadioButton>(R.id.rbSingle)
        val rbMarried = rootView.findViewById<RadioButton>(R.id.rbMarried)
        val rbWidowed = rootView.findViewById<RadioButton>(R.id.rbWidowed)

        val rgPension = rootView.findViewById<RadioGroup>(R.id.rgPension)
        val rbPensionYes = rootView.findViewById<RadioButton>(R.id.rbPensionYes)
        val rbPensionNo = rootView.findViewById<RadioButton>(R.id.rbPensionNo)

        val rgInsurance = rootView.findViewById<RadioGroup>(R.id.rgInsurance)
        val rbInsuranceYes = rootView.findViewById<RadioButton>(R.id.rbInsuranceYes)
        val rbInsuranceNo = rootView.findViewById<RadioButton>(R.id.rbInsuranceNo)

        val btnSave = rootView.findViewById<Button>(R.id.btnSaveProfile)

        // 2. Load Existing Data (PRE-FILL LOGIC)
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""

        // Load Basic Text Fields
        etFirstName.setText(prefs.getString("${currentUser}_FIRST_NAME", ""))
        etLastName.setText(prefs.getString("${currentUser}_LAST_NAME", ""))
        etAge.setText(prefs.getString("${currentUser}_AGE", ""))
        etHobby.setText(prefs.getString("${currentUser}_HOBBY", ""))
        etPensionAmount.setText(prefs.getString("${currentUser}_PENSION_AMOUNT", ""))

        // Load Insurance Text Fields
        etInsuranceCompany.setText(prefs.getString("${currentUser}_INS_COMPANY", ""))
        etInsurancePlan.setText(prefs.getString("${currentUser}_INS_PLAN", ""))
        etInsurancePremium.setText(prefs.getString("${currentUser}_INS_PREMIUM", ""))

        // --- PRE-FILL SEX ---
        val savedSex = prefs.getString("${currentUser}_SEX", "")
        if (savedSex == "Male") rbMale.isChecked = true
        else if (savedSex == "Female") rbFemale.isChecked = true

        // --- PRE-FILL MARITAL STATUS ---
        val savedMarital = prefs.getString("${currentUser}_MARITAL_STATUS", "")
        when (savedMarital) {
            "Single" -> rbSingle.isChecked = true
            "Married" -> rbMarried.isChecked = true
            "Widowed" -> rbWidowed.isChecked = true
        }

        // --- PRE-FILL PENSION ---
        val hasPension = prefs.getBoolean("${currentUser}_HAS_PENSION", false)
        if (hasPension) {
            rbPensionYes.isChecked = true
            layoutPensionDetails.visibility = View.VISIBLE
        } else {
            rbPensionNo.isChecked = true
            layoutPensionDetails.visibility = View.GONE
        }

        // --- PRE-FILL INSURANCE ---
        val hasInsurance = prefs.getBoolean("${currentUser}_HAS_INSURANCE", false)
        if (hasInsurance) {
            rbInsuranceYes.isChecked = true
            layoutInsuranceDetails.visibility = View.VISIBLE
        } else {
            rbInsuranceNo.isChecked = true
            layoutInsuranceDetails.visibility = View.GONE
        }

        // 3. Listeners (Toggle Visibility)

        // Pension Listener
        rgPension.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbPensionYes) {
                layoutPensionDetails.visibility = View.VISIBLE
            } else {
                layoutPensionDetails.visibility = View.GONE
            }
        }

        // Insurance Listener (FIXED)
        rgInsurance.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbInsuranceYes) {
                layoutInsuranceDetails.visibility = View.VISIBLE
            } else {
                layoutInsuranceDetails.visibility = View.GONE
            }
        }

        // 4. Save Button Logic
        btnSave.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val age = etAge.text.toString()
            val hobby = etHobby.text.toString()
            val pensionAmt = etPensionAmount.text.toString()

            // Insurance Data
            val insCompany = etInsuranceCompany.text.toString()
            val insPlan = etInsurancePlan.text.toString()
            val insPremium = etInsurancePremium.text.toString()

            // Get selected radio text
            var sex = ""
            if (rbMale.isChecked) sex = "Male"
            else if (rbFemale.isChecked) sex = "Female"

            var maritalStatus = ""
            if (rbSingle.isChecked) maritalStatus = "Single"
            else if (rbMarried.isChecked) maritalStatus = "Married"
            else if (rbWidowed.isChecked) maritalStatus = "Widowed"

            val hasPensionSelected = rbPensionYes.isChecked
            val hasInsuranceSelected = rbInsuranceYes.isChecked

            if (firstName.isEmpty() || age.isEmpty()) {
                Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
            } else {
                val editor = prefs.edit()
                editor.putString("${currentUser}_FIRST_NAME", firstName)
                editor.putString("${currentUser}_LAST_NAME", lastName)
                editor.putString("${currentUser}_AGE", age)
                editor.putString("${currentUser}_HOBBY", hobby)
                editor.putString("${currentUser}_SEX", sex)
                editor.putString("${currentUser}_MARITAL_STATUS", maritalStatus)

                // Save Pension
                editor.putBoolean("${currentUser}_HAS_PENSION", hasPensionSelected)
                editor.putString("${currentUser}_PENSION_AMOUNT", if (hasPensionSelected) pensionAmt else "0")

                // Save Insurance
                editor.putBoolean("${currentUser}_HAS_INSURANCE", hasInsuranceSelected)
                if (hasInsuranceSelected) {
                    editor.putString("${currentUser}_INS_COMPANY", insCompany)
                    editor.putString("${currentUser}_INS_PLAN", insPlan)
                    editor.putString("${currentUser}_INS_PREMIUM", insPremium)
                } else {
                    // Clear if they selected No
                    editor.putString("${currentUser}_INS_COMPANY", "")
                    editor.putString("${currentUser}_INS_PLAN", "")
                    editor.putString("${currentUser}_INS_PREMIUM", "0")
                }

                editor.putBoolean("PROFILE_COMPLETE_$currentUser", true)
                editor.apply()

                Toast.makeText(context, "Profile Saved!", Toast.LENGTH_SHORT).show()

                // Navigate to Home
                if (activity is MainActivity) {
                    (activity as MainActivity).updateNavigationVisibility()
                    (activity as MainActivity).showFragment(WelcomeFragment())
                    (activity as MainActivity).switchToTab(R.id.nav_home)
                }
            }
        }

        return rootView
    }
}