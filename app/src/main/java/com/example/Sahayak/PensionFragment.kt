package com.example.sahayak

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sahayak.R

class PensionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pension, container, false)
        val tvStatus: TextView = view.findViewById(R.id.tvPensionStatus)
        val rvHistory: RecyclerView = view.findViewById(R.id.rvPensionHistory)
        val tvHistoryTitle: TextView = view.findViewById(R.id.tvPensionHistoryTitle)

        val prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)

        // --- UPDATED LOGIC ---
        // 1. Get the current user
        val currentUser = prefs?.getString("CURRENT_USER", "") ?: ""

        // 2. Check if THIS specific user has pension
        val hasPension = prefs?.getBoolean("HAS_PENSION_$currentUser", false) ?: false

        if (hasPension) {
            // 3. Get the amount for THIS specific user
            val amount = prefs?.getString("PENSION_AMOUNT_$currentUser", "N/A")
            tvStatus.text = getString(R.string.pension_status_yes, amount)

            // Create dummy history for 2025
            val history = listOf(
                PensionHistoryItem("January 2025", getString(R.string.status_credited)),
                PensionHistoryItem("February 2025", getString(R.string.status_credited)),
                PensionHistoryItem("March 2025", getString(R.string.status_credited)),
                PensionHistoryItem("April 2025", getString(R.string.status_credited)),
                PensionHistoryItem("May 2025", getString(R.string.status_credited)),
                PensionHistoryItem("June 2025", getString(R.string.status_credited)),
                PensionHistoryItem("July 2025", getString(R.string.status_credited)),
                PensionHistoryItem("August 2025", getString(R.string.status_credited)),
                PensionHistoryItem("September 2025", getString(R.string.status_credited)),
                PensionHistoryItem("October 2025", getString(R.string.status_credited)),
                PensionHistoryItem("November 2025", getString(R.string.status_pending)),
                PensionHistoryItem("December 2025", getString(R.string.status_upcoming))
            )

            rvHistory.layoutManager = LinearLayoutManager(activity)
            rvHistory.adapter = PensionHistoryAdapter(history)

            // Make sure list is visible
            tvHistoryTitle.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE

        } else {
            // No pension for this user
            tvStatus.text = getString(R.string.pension_status_no)

            // Hide the list and title
            tvHistoryTitle.visibility = View.GONE
            rvHistory.visibility = View.GONE
        }
        // ---------------------

        return view
    }
}