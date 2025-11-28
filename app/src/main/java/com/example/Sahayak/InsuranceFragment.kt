package com.example.sahayak

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class InsuranceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_insurance, container, false)
        val tvStatus: TextView = view.findViewById(R.id.tvInsuranceStatus)
        val tvCompany: TextView = view.findViewById(R.id.tvInsuranceCompany)
        val tvPlan: TextView = view.findViewById(R.id.tvInsurancePlan)
        val tvPremium: TextView = view.findViewById(R.id.tvInsurancePremium)

        val prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)

        // --- UPDATED LOGIC ---
        // 1. Get the current user
        val currentUser = prefs?.getString("CURRENT_USER", "") ?: ""

        // 2. Check if THIS specific user has insurance
        val hasInsurance = prefs?.getBoolean("HAS_INSURANCE_$currentUser", false) ?: false

        if (hasInsurance) {
            tvStatus.visibility = View.GONE // No status text needed if we have data

            // 3. Fetch specific details for THIS user
            val company = prefs?.getString("INSURANCE_COMPANY_$currentUser", "N/A")
            val plan = prefs?.getString("INSURANCE_PLAN_$currentUser", "N/A")
            val premium = prefs?.getString("INSURANCE_PREMIUM_$currentUser", "N/A")

            tvCompany.text = "${getString(R.string.insurance_company)} $company"
            tvPlan.text = "${getString(R.string.insurance_plan)} $plan"
            tvPremium.text = "${getString(R.string.insurance_premium)} $premium"

            // Ensure details are visible
            tvCompany.visibility = View.VISIBLE
            tvPlan.visibility = View.VISIBLE
            tvPremium.visibility = View.VISIBLE

        } else {
            // No insurance for this user
            tvStatus.text = getString(R.string.insurance_status_no)
            tvStatus.visibility = View.VISIBLE

            // Hide the details
            tvCompany.visibility = View.GONE
            tvPlan.visibility = View.GONE
            tvPremium.visibility = View.GONE
        }
        // ---------------------

        return view
    }
}