package com.example.sahayak

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class InsuranceFragment : Fragment() {

    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_insurance, container, false)

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Initialize Views
        val cardDetails = view.findViewById<CardView>(R.id.cardInsuranceDetails)
        val tvNoRecord = view.findViewById<TextView>(R.id.tvNoInsurance)

        val tvCompany = view.findViewById<TextView>(R.id.tvInsCompany)
        val tvPlan = view.findViewById<TextView>(R.id.tvInsPlan)
        val tvPremium = view.findViewById<TextView>(R.id.tvInsPremium)

        // Labels (to ensure translation)
        val tvHeader = view.findViewById<TextView>(R.id.tvInsuranceHeader)
        val tvStatusLabel = view.findViewById<TextView>(R.id.tvStatusLabel)
        val tvActiveStatus = view.findViewById<TextView>(R.id.tvActiveStatus)
        val tvCompanyLabel = view.findViewById<TextView>(R.id.tvCompanyLabel)
        val tvPlanLabel = view.findViewById<TextView>(R.id.tvPlanLabel)
        val tvPremiumLabel = view.findViewById<TextView>(R.id.tvPremiumLabel)

        // 2. Set Localized Text (Use Strings from XML)
        tvHeader.text = getString(R.string.insurance_title)
        tvNoRecord.text = getString(R.string.no_insurance_record)
        tvStatusLabel.text = getString(R.string.insurance_status_label)
        tvActiveStatus.text = getString(R.string.status_active)
        tvCompanyLabel.text = getString(R.string.company_label)
        tvPlanLabel.text = getString(R.string.plan_label)
        tvPremiumLabel.text = getString(R.string.premium_label)

        // 3. Get Current User Data
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""
        val hasInsurance = prefs.getBoolean("${currentUser}_HAS_INSURANCE", false)

        if (hasInsurance) {
            cardDetails.visibility = View.VISIBLE
            tvNoRecord.visibility = View.GONE

            val company = prefs.getString("${currentUser}_INS_COMPANY", "N/A")
            val plan = prefs.getString("${currentUser}_INS_PLAN", "N/A")
            val premium = prefs.getString("${currentUser}_INS_PREMIUM", "0")

            tvCompany.text = company
            tvPlan.text = plan
            tvPremium.text = "₹$premium"
        } else {
            cardDetails.visibility = View.GONE
            tvNoRecord.visibility = View.VISIBLE
        }

        return view
    }
}