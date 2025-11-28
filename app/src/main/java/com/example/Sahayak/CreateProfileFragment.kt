package com.example.sahayak

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class CreateProfileFragment : Fragment() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etAge: EditText
    private lateinit var etHobby: EditText
    private lateinit var rgSex: RadioGroup
    private lateinit var rgMaritalStatus: RadioGroup
    private lateinit var rgPension: RadioGroup
    private lateinit var layoutPension: LinearLayout
    private lateinit var etPensionAmount: EditText
    private lateinit var rgInsurance: RadioGroup
    private lateinit var layoutInsurance: LinearLayout
    private lateinit var etInsuranceCompany: EditText
    private lateinit var etInsurancePlan: EditText
    private lateinit var etInsurancePremium: EditText
    private lateinit var btnSaveProfile: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_profile, container, false)

        etFirstName = view.findViewById(R.id.etFirstName)
        etLastName = view.findViewById(R.id.etLastName)
        etAge = view.findViewById(R.id.etAge)
        etHobby = view.findViewById(R.id.etHobby)
        rgSex = view.findViewById(R.id.rgSex)
        rgMaritalStatus = view.findViewById(R.id.rgMaritalStatus)
        rgPension = view.findViewById(R.id.rgPension)
        layoutPension = view.findViewById(R.id.layoutPensionDetails)
        etPensionAmount = view.findViewById(R.id.etPensionAmount)
        rgInsurance = view.findViewById(R.id.rgInsurance)
        layoutInsurance = view.findViewById(R.id.layoutInsuranceDetails)
        etInsuranceCompany = view.findViewById(R.id.etInsuranceCompany)
        etInsurancePlan = view.findViewById(R.id.etInsurancePlan)
        etInsurancePremium = view.findViewById(R.id.etInsurancePremium)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)

        // Logic to show/hide sections
        rgPension.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbPensionYes) {
                layoutPension.visibility = View.VISIBLE
            } else {
                layoutPension.visibility = View.GONE
            }
        }
        rgInsurance.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbInsuranceYes) {
                layoutInsurance.visibility = View.VISIBLE
            } else {
                layoutInsurance.visibility = View.GONE
            }
        }

        loadProfileData()

        btnSaveProfile.setOnClickListener {
            if (saveProfileData()) {
                (activity as MainActivity).showFragment(WelcomeFragment())
            }
        }
        return view
    }

    private fun loadProfileData() {
        val prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE) ?: return

        // Get the current user so we load THEIR specific data
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""

        // Check if this specific user has data
        val isProfileComplete = prefs.getBoolean("PROFILE_COMPLETE_$currentUser", false)
        if (!isProfileComplete) return // If new user, keep fields empty

        // --- UPDATED LOADING LOGIC: USE KEYS WITH USERNAME ---
        etFirstName.setText(prefs.getString("FIRST_NAME_$currentUser", ""))
        etLastName.setText(prefs.getString("LAST_NAME_$currentUser", ""))
        etAge.setText(prefs.getString("AGE_$currentUser", ""))
        etHobby.setText(prefs.getString("HOBBY_$currentUser", ""))

        when (prefs.getString("SEX_$currentUser", "")) {
            getString(R.string.male) -> rgSex.check(R.id.rbMale)
            getString(R.string.female) -> rgSex.check(R.id.rbFemale)
        }

        when (prefs.getString("MARITAL_STATUS_$currentUser", "")) {
            getString(R.string.single) -> rgMaritalStatus.check(R.id.rbSingle)
            getString(R.string.married) -> rgMaritalStatus.check(R.id.rbMarried)
            getString(R.string.widowed) -> rgMaritalStatus.check(R.id.rbWidowed)
        }

        if (prefs.getBoolean("HAS_PENSION_$currentUser", false)) {
            rgPension.check(R.id.rbPensionYes)
            layoutPension.visibility = View.VISIBLE
            etPensionAmount.setText(prefs.getString("PENSION_AMOUNT_$currentUser", ""))
        } else {
            rgPension.check(R.id.rbPensionNo)
            layoutPension.visibility = View.GONE
        }

        if (prefs.getBoolean("HAS_INSURANCE_$currentUser", false)) {
            rgInsurance.check(R.id.rbInsuranceYes)
            layoutInsurance.visibility = View.VISIBLE
            etInsuranceCompany.setText(prefs.getString("INSURANCE_COMPANY_$currentUser", ""))
            etInsurancePlan.setText(prefs.getString("INSURANCE_PLAN_$currentUser", ""))
            etInsurancePremium.setText(prefs.getString("INSURANCE_PREMIUM_$currentUser", ""))
        } else {
            rgInsurance.check(R.id.rbInsuranceNo)
            layoutInsurance.visibility = View.GONE
        }
    }

    private fun saveProfileData(): Boolean {
        val firstName = etFirstName.text.toString()
        if (firstName.isEmpty()) {
            Toast.makeText(activity, getString(R.string.profile_toast_fill_fields), Toast.LENGTH_LONG).show()
            return false
        }

        val prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE) ?: return false

        // Get the current user so we save to THEIR specific "Locker"
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""

        with (prefs.edit()) {
            // --- UPDATED SAVING LOGIC: APPEND USERNAME TO EVERY KEY ---
            putString("FIRST_NAME_$currentUser", firstName)
            putString("LAST_NAME_$currentUser", etLastName.text.toString())
            putString("AGE_$currentUser", etAge.text.toString())
            putString("HOBBY_$currentUser", etHobby.text.toString())

            val sexId = rgSex.checkedRadioButtonId
            if (sexId == R.id.rbMale) putString("SEX_$currentUser", getString(R.string.male))
            else if (sexId == R.id.rbFemale) putString("SEX_$currentUser", getString(R.string.female))

            val maritalId = rgMaritalStatus.checkedRadioButtonId
            if (maritalId == R.id.rbSingle) putString("MARITAL_STATUS_$currentUser", getString(R.string.single))
            else if (maritalId == R.id.rbMarried) putString("MARITAL_STATUS_$currentUser", getString(R.string.married))
            else if (maritalId == R.id.rbWidowed) putString("MARITAL_STATUS_$currentUser", getString(R.string.widowed))

            val hasPension = (rgPension.checkedRadioButtonId == R.id.rbPensionYes)
            putBoolean("HAS_PENSION_$currentUser", hasPension)
            if (hasPension) {
                putString("PENSION_AMOUNT_$currentUser", etPensionAmount.text.toString())
            }

            val hasInsurance = (rgInsurance.checkedRadioButtonId == R.id.rbInsuranceYes)
            putBoolean("HAS_INSURANCE_$currentUser", hasInsurance)
            if (hasInsurance) {
                putString("INSURANCE_COMPANY_$currentUser", etInsuranceCompany.text.toString())
                putString("INSURANCE_PLAN_$currentUser", etInsurancePlan.text.toString())
                putString("INSURANCE_PREMIUM_$currentUser", etInsurancePremium.text.toString())
            }

            putBoolean("PROFILE_COMPLETE_$currentUser", true)
            apply()
        }

        Toast.makeText(activity, getString(R.string.profile_toast_saved), Toast.LENGTH_LONG).show()
        return true
    }
}