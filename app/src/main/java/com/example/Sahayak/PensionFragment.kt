package com.example.sahayak

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PensionFragment : Fragment() {

    private lateinit var adapter: PensionAdapter
    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pension, container, false)

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Check User Pension Status
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""
        val hasPension = prefs.getBoolean("${currentUser}_HAS_PENSION", false)
        val pensionAmount = prefs.getString("${currentUser}_PENSION_AMOUNT", "0") ?: "0"

        val layoutContent = view.findViewById<LinearLayout>(R.id.layoutPensionContent)
        val tvNoRecord = view.findViewById<TextView>(R.id.tvNoPensionMessage)
        val tvAmount = view.findViewById<TextView>(R.id.tvPensionAmount)
        val tvHeader = view.findViewById<TextView>(R.id.tvPensionTitle)
        val tvSelectYear = view.findViewById<TextView>(R.id.tvSelectYearLabel)

        // Set Localized Static Text
        tvHeader.text = getString(R.string.pension_title)
        tvNoRecord.text = getString(R.string.no_pension_record)
        tvSelectYear.text = getString(R.string.select_year)

        if (!hasPension) {
            layoutContent.visibility = View.GONE
            tvNoRecord.visibility = View.VISIBLE
            return view
        } else {
            layoutContent.visibility = View.VISIBLE
            tvNoRecord.visibility = View.GONE
            tvAmount.text = getString(R.string.monthly_pension_text, pensionAmount)
        }

        // 2. Setup RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvPensionHistory)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PensionAdapter(emptyList())
        recyclerView.adapter = adapter

        // 3. Setup Spinner
        val spinner = view.findViewById<Spinner>(R.id.spinnerYear)
        val startYear = 1990
        val endYear = 2030
        val years = (startYear..endYear).toList()

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        val currentCalendar = Calendar.getInstance()
        val realCurrentYear = currentCalendar.get(Calendar.YEAR)
        val defaultIndex = years.indexOf(realCurrentYear)
        if (defaultIndex >= 0) spinner.setSelection(defaultIndex)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedYear = years[position]
                updateListForYear(selectedYear)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }

    private fun updateListForYear(selectedYear: Int) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        val newList = ArrayList<PensionRecord>()

        // Get Month Name in CURRENT Language
        val currentLocale = resources.configuration.locales[0]
        val monthFormat = SimpleDateFormat("MMMM", currentLocale)

        for (i in 0..11) {
            calendar.set(Calendar.MONTH, i)
            val monthName = monthFormat.format(calendar.time)

            val monthNumber = String.format("%02d", i + 1)
            val dateString = "01/$monthNumber/$selectedYear"

            val creditedText = getString(R.string.status_credited_date, dateString)
            val upcomingText = getString(R.string.status_upcoming)

            val (statusType, displayText) = when {
                selectedYear < currentYear -> Pair("Credited", creditedText)
                selectedYear > currentYear -> Pair("Upcoming", upcomingText)
                else -> {
                    // --- THE FIX IS HERE ---
                    // Used to be 'i < currentMonth', now 'i <= currentMonth'
                    // This forces the current month (Jan) to be CREDITED immediately.
                    if (i <= currentMonth) {
                        Pair("Credited", creditedText)
                    } else {
                        Pair("Upcoming", upcomingText)
                    }
                }
            }
            newList.add(PensionRecord(monthName, displayText, statusType))
        }

        adapter.updateData(newList)
    }
}