package com.example.sahayak

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class CreateProfileFragment : Fragment() {

    private val PREFS_NAME = "SeniorCareApp"
    private val KEY_FIRST_NAME = "FIRST_NAME"
    private val KEY_LAST_NAME = "LAST_NAME"
    private val KEY_AGE = "AGE"
    private val KEY_HOBBY = "HOBBY"
    private val KEY_SEX = "SEX" // Added Key
    private val KEY_MARITAL_STATUS = "MARITAL_STATUS" // Added Key
    private val KEY_RECEIVES_PENSION = "HAS_PENSION"
    private val KEY_PENSION_AMOUNT = "PENSION_AMOUNT"
    private val KEY_HAS_INSURANCE = "HAS_INSURANCE"
    private val KEY_INSURANCE_COMPANY = "INSURANCE_COMPANY"
    private val KEY_INSURANCE_PLAN = "INSURANCE_PLAN"
    private val KEY_INSURANCE_PREMIUM = "INSURANCE_PREMIUM"

    private lateinit var prefs: SharedPreferences
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_profile, container, false)
        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

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

        rgPension.setOnCheckedChangeListener { _, id -> layoutPension.visibility = if (id == R.id.rbPensionYes) View.VISIBLE else View.GONE }
        rgInsurance.setOnCheckedChangeListener { _, id -> layoutInsurance.visibility = if (id == R.id.rbInsuranceYes) View.VISIBLE else View.GONE }

        loadProfileData()

        btnSaveProfile.setOnClickListener { validateAndSave(view) } // Pass view to get radio button text
        return view
    }

    private fun getUserKey(key: String): String {
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""
        return "${currentUser}_$key"
    }

    private fun loadProfileData() {
        etFirstName.setText(prefs.getString(getUserKey(KEY_FIRST_NAME), ""))
        etLastName.setText(prefs.getString(getUserKey(KEY_LAST_NAME), ""))
        etAge.setText(prefs.getString(getUserKey(KEY_AGE), ""))
        etHobby.setText(prefs.getString(getUserKey(KEY_HOBBY), ""))

        // Note: Loading logic for RadioGroups (Sex/Marital) can be added here if needed in the future

        val hasPension = prefs.getBoolean(getUserKey(KEY_RECEIVES_PENSION), false)
        if (hasPension) {
            rgPension.check(R.id.rbPensionYes)
            etPensionAmount.setText(prefs.getString(getUserKey(KEY_PENSION_AMOUNT), ""))
        } else {
            rgPension.check(R.id.rbPensionNo)
        }

        val hasInsurance = prefs.getBoolean(getUserKey(KEY_HAS_INSURANCE), false)
        if (hasInsurance) {
            rgInsurance.check(R.id.rbInsuranceYes)
            etInsuranceCompany.setText(prefs.getString(getUserKey(KEY_INSURANCE_COMPANY), ""))
            etInsurancePlan.setText(prefs.getString(getUserKey(KEY_INSURANCE_PLAN), ""))
            etInsurancePremium.setText(prefs.getString(getUserKey(KEY_INSURANCE_PREMIUM), ""))
        } else {
            rgInsurance.check(R.id.rbInsuranceNo)
        }
    }

    private fun validateAndSave(view: View) {
        val fName = etFirstName.text.toString().trim()
        val lName = etLastName.text.toString().trim()
        val age = etAge.text.toString().trim()

        // Check which radio buttons are selected (-1 means nothing selected)
        val selectedSexId = rgSex.checkedRadioButtonId
        val selectedMaritalId = rgMaritalStatus.checkedRadioButtonId

        // --- UPDATED VALIDATION ---
        // Now checks if Sex OR Marital Status is NOT selected (equals -1)
        if (fName.isEmpty() || lName.isEmpty() || age.isEmpty() || selectedSexId == -1 || selectedMaritalId == -1) {
            Toast.makeText(activity, getString(R.string.please_fill_all_fields), Toast.LENGTH_SHORT).show()
            return
        }
        // --------------------------

        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""

        with (prefs.edit()) {
            putString(getUserKey(KEY_FIRST_NAME), fName)
            putString(getUserKey(KEY_LAST_NAME), lName)
            putString(getUserKey(KEY_AGE), age)
            putString(getUserKey(KEY_HOBBY), etHobby.text.toString())

            // Save Sex
            val sexButton = view.findViewById<RadioButton>(selectedSexId)
            putString(getUserKey(KEY_SEX), sexButton.text.toString())

            // Save Marital Status
            val maritalButton = view.findViewById<RadioButton>(selectedMaritalId)
            putString(getUserKey(KEY_MARITAL_STATUS), maritalButton.text.toString())

            val hasPension = (rgPension.checkedRadioButtonId == R.id.rbPensionYes)
            putBoolean(getUserKey(KEY_RECEIVES_PENSION), hasPension)
            if (hasPension) putString(getUserKey(KEY_PENSION_AMOUNT), etPensionAmount.text.toString())
            else putString(getUserKey(KEY_PENSION_AMOUNT), "")

            val hasInsurance = (rgInsurance.checkedRadioButtonId == R.id.rbInsuranceYes)
            putBoolean(getUserKey(KEY_HAS_INSURANCE), hasInsurance)
            if (hasInsurance) {
                putString(getUserKey(KEY_INSURANCE_COMPANY), etInsuranceCompany.text.toString())
                putString(getUserKey(KEY_INSURANCE_PLAN), etInsurancePlan.text.toString())
                putString(getUserKey(KEY_INSURANCE_PREMIUM), etInsurancePremium.text.toString())
            } else {
                putString(getUserKey(KEY_INSURANCE_COMPANY), "")
                putString(getUserKey(KEY_INSURANCE_PLAN), "")
                putString(getUserKey(KEY_INSURANCE_PREMIUM), "")
            }

            putBoolean("PROFILE_COMPLETE_$currentUser", true)
            commit()
        }

        Toast.makeText(activity, getString(R.string.profile_toast_saved), Toast.LENGTH_SHORT).show()
        (activity as MainActivity).updateNavigationVisibility()
        (activity as MainActivity).showFragment(WelcomeFragment())
    }
}