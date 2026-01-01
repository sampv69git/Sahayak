package com.example.sahayak

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Updated Data Class: 'displayText' holds the date, 'statusType' determines color
data class PensionRecord(val month: String, val displayText: String, val statusType: String)

class PensionAdapter(private var pensionList: List<PensionRecord>) :
    RecyclerView.Adapter<PensionAdapter.PensionViewHolder>() {

    class PensionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMonth: TextView = view.findViewById(R.id.tvMonthYear)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PensionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pension_row, parent, false)
        return PensionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PensionViewHolder, position: Int) {
        val item = pensionList[position]

        holder.tvMonth.text = item.month
        holder.tvStatus.text = item.displayText

        // Color Logic based on statusType
        when (item.statusType) {
            "Credited" -> holder.tvStatus.setTextColor(Color.parseColor("#388E3C")) // Green
            "Pending" -> holder.tvStatus.setTextColor(Color.parseColor("#F57C00"))  // Orange
            "Upcoming" -> holder.tvStatus.setTextColor(Color.parseColor("#757575")) // Grey
        }
    }

    override fun getItemCount() = pensionList.size

    fun updateData(newList: List<PensionRecord>) {
        pensionList = newList
        notifyDataSetChanged()
    }
}