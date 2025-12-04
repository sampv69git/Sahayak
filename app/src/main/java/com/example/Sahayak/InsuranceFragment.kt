package com.example.sahayak

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class InsuranceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_insurance, container, false)

        val prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)
        val currentUser = prefs?.getString("CURRENT_USER", "") ?: ""

        val hasInsurance = prefs?.getBoolean("${currentUser}_HAS_INSURANCE", false) ?: false

        // IDs must match your XML
        val tvStatus = view.findViewById<TextView>(R.id.tvInsuranceStatus) // Make sure this ID exists in XML
        val tvCompany = view.findViewById<TextView>(R.id.tvInsuranceCompany)
        val tvPlan = view.findViewById<TextView>(R.id.tvInsurancePlan)
        val tvPremium = view.findViewById<TextView>(R.id.tvInsurancePremium)

        if (hasInsurance) {
            val company = prefs?.getString("${currentUser}_INSURANCE_COMPANY", "")
            val plan = prefs?.getString("${currentUser}_INSURANCE_PLAN", "")
            val premium = prefs?.getString("${currentUser}_INSURANCE_PREMIUM", "")

            // FIX: Use getString with arguments to support multiple languages
            tvStatus.text = getString(R.string.status_active)
            tvCompany.text = getString(R.string.ins_company_format, company)
            tvPlan.text = getString(R.string.ins_plan_format, plan)
            tvPremium.text = getString(R.string.ins_premium_format, premium)
        } else {
            // If no insurance, show "Not Insured" and "N/A"
            tvStatus.text = getString(R.string.status_inactive)
            tvCompany.text = getString(R.string.ins_company_format, getString(R.string.status_na))
            tvPlan.text = getString(R.string.ins_plan_format, getString(R.string.status_na))
            tvPremium.text = getString(R.string.ins_premium_format, getString(R.string.status_na))
        }

        return view
    }
}