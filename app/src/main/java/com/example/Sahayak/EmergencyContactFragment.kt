package com.example.sahayak

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class EmergencyContactFragment : Fragment() {

    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_emergency_contact, container, false)
        prefs = activity?.getSharedPreferences("SeniorCareApp", Context.MODE_PRIVATE)!!

        val btnCallPolice: Button = view.findViewById(R.id.btnPolice)
        val btnCallAmbulance: Button = view.findViewById(R.id.btnAmbulance)
        val btnCallSon: Button = view.findViewById(R.id.btnCallSon)
        val btnCallDaughter: Button = view.findViewById(R.id.btnCallDaughter)
        val btnCallSpouse: Button = view.findViewById(R.id.btnCallSpouse)
        val btnSetContacts: Button = view.findViewById(R.id.btnSetContacts)

        // Public numbers
        btnCallPolice.setOnClickListener { dialNumber("100") }
        btnCallAmbulance.setOnClickListener { dialNumber("102") }

        // Family numbers
        btnCallSon.setOnClickListener { dialNumber(prefs.getString("SON_NUMBER", "")!!) }
        btnCallDaughter.setOnClickListener { dialNumber(prefs.getString("DAUGHTER_NUMBER", "")!!) }
        btnCallSpouse.setOnClickListener { dialNumber(prefs.getString("SPOUSE_NUMBER", "")!!) }

        // Set numbers button
        btnSetContacts.setOnClickListener {
            showSetContactsDialog()
        }
        return view
    }

    private fun dialNumber(number: String) {
        if (number.isEmpty()) {
            Toast.makeText(activity, getString(R.string.number_not_set), Toast.LENGTH_LONG).show()
            return;
        }
        // Use ACTION_DIAL to open the dialer, not CALL_PHONE to call directly
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun showSetContactsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        // Create the dialog layout file (next step)
        val dialogView = inflater.inflate(R.layout.dialog_edit_contacts, null)

        val etSon: EditText = dialogView.findViewById(R.id.etSonNumber)
        val etDaughter: EditText = dialogView.findViewById(R.id.etDaughterNumber)
        val etSpouse: EditText = dialogView.findViewById(R.id.etSpouseNumber)

        // Load existing numbers into the dialog
        etSon.setText(prefs.getString("SON_NUMBER", ""))
        etDaughter.setText(prefs.getString("DAUGHTER_NUMBER", ""))
        etSpouse.setText(prefs.getString("SPOUSE_NUMBER", ""))

        builder.setView(dialogView)
            .setTitle(getString(R.string.dialog_set_contacts_title))
            .setPositiveButton(getString(R.string.save)) { dialog, id ->
                // Save the new numbers
                with (prefs.edit()) {
                    putString("SON_NUMBER", etSon.text.toString())
                    putString("DAUGHTER_NUMBER", etDaughter.text.toString())
                    putString("SPOUSE_NUMBER", etSpouse.text.toString())
                    apply()
                }
                Toast.makeText(activity, getString(R.string.contacts_saved), Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }
}