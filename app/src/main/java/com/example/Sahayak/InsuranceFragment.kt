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
        val hasInsurance = prefs?.getBoolean("HAS_INSURANCE", false) ?: false

        if (hasInsurance) {
            tvStatus.visibility = View.GONE // No status, just show details
            tvCompany.text = "${getString(R.string.insurance_company)} ${prefs?.getString("INSURANCE_COMPANY", "N/A")}"
            tvPlan.text = "${getString(R.string.insurance_plan)} ${prefs?.getString("INSURANCE_PLAN", "N/A")}"
            tvPremium.text = "${getString(R.string.insurance_premium)} ${prefs?.getString("INSURANCE_PREMIUM", "N/A")}"
        } else {
            // No insurance
            tvStatus.text = getString(R.string.insurance_status_no)
            tvCompany.visibility = View.GONE
            tvPlan.visibility = View.GONE
            tvPremium.visibility = View.GONE
        }
        return view
    }
}