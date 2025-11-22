package com.example.sahayak

import android.content.Context
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
import com.example.sahayak.R

class CreateProfileFragment : Fragment() {

    // Declare all views
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

        // Find all the views
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

        // --- Logic to show/hide sections ---
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

        // --- NEW: Load existing data ---
        loadProfileData()

        // --- Save Button Logic ---
        btnSaveProfile.setOnClickListener {
            if (saveProfileData()) {
                // If save is successful, go back to the welcome screen
                (activity as MainActivity).showFragment(WelcomeFragment())
            }
        }
        return view
    }

    private fun loadProfileData() {
        val prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE) ?: return

        etFirstName.setText(prefs.getString("FIRST_NAME", ""))
        etLastName.setText(prefs.getString("LAST_NAME", ""))
        etAge.setText(prefs.getString("AGE", ""))
        etHobby.setText(prefs.getString("HOBBY", ""))

        // Pre-select sex
        when (prefs.getString("SEX", "")) {
            getString(R.string.male) -> rgSex.check(R.id.rbMale)
            getString(R.string.female) -> rgSex.check(R.id.rbFemale)
        }

        // Pre-select marital status
        when (prefs.getString("MARITAL_STATUS", "")) {
            getString(R.string.single) -> rgMaritalStatus.check(R.id.rbSingle)
            getString(R.string.married) -> rgMaritalStatus.check(R.id.rbMarried)
            getString(R.string.widowed) -> rgMaritalStatus.check(R.id.rbWidowed)
        }

        // Pre-select pension
        if (prefs.getBoolean("HAS_PENSION", false)) {
            rgPension.check(R.id.rbPensionYes)
            layoutPension.visibility = View.VISIBLE
            etPensionAmount.setText(prefs.getString("PENSION_AMOUNT", ""))
        } else {
            rgPension.check(R.id.rbPensionNo)
            layoutPension.visibility = View.GONE
        }

        // Pre-select insurance
        if (prefs.getBoolean("HAS_INSURANCE", false)) {
            rgInsurance.check(R.id.rbInsuranceYes)
            layoutInsurance.visibility = View.VISIBLE
            etInsuranceCompany.setText(prefs.getString("INSURANCE_COMPANY", ""))
            etInsurancePlan.setText(prefs.getString("INSURANCE_PLAN", ""))
            etInsurancePremium.setText(prefs.getString("INSURANCE_PREMIUM", ""))
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

        with (prefs.edit()) {
            putString("FIRST_NAME", firstName)
            putString("LAST_NAME", etLastName.text.toString())
            putString("AGE", etAge.text.toString())
            putString("HOBBY", etHobby.text.toString())

            val sexId = rgSex.checkedRadioButtonId
            if (sexId == R.id.rbMale) putString("SEX", getString(R.string.male))
            else if (sexId == R.id.rbFemale) putString("SEX", getString(R.string.female))

            val maritalId = rgMaritalStatus.checkedRadioButtonId
            if (maritalId == R.id.rbSingle) putString("MARITAL_STATUS", getString(R.string.single))
            else if (maritalId == R.id.rbMarried) putString("MARITAL_STATUS", getString(R.string.married))
            else if (maritalId == R.id.rbWidowed) putString("MARITAL_STATUS", getString(R.string.widowed))

            val hasPension = (rgPension.checkedRadioButtonId == R.id.rbPensionYes)
            putBoolean("HAS_PENSION", hasPension)
            if (hasPension) {
                putString("PENSION_AMOUNT", etPensionAmount.text.toString())
            }

            val hasInsurance = (rgInsurance.checkedRadioButtonId == R.id.rbInsuranceYes)
            putBoolean("HAS_INSURANCE", hasInsurance)
            if (hasInsurance) {
                putString("INSURANCE_COMPANY", etInsuranceCompany.text.toString())
                putString("INSURANCE_PLAN", etInsurancePlan.text.toString())
                putString("INSURANCE_PREMIUM", etInsurancePremium.text.toString())
            }

            putBoolean("PROFILE_COMPLETE", true)
            apply()
        }

        Toast.makeText(activity, getString(R.string.profile_toast_saved), Toast.LENGTH_LONG).show()
        return true
    }
}