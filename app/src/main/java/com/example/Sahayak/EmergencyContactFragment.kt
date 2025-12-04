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
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class EmergencyContactFragment : Fragment() {

    private lateinit var layoutContainer: LinearLayout
    private lateinit var btnAddContact: Button
    private lateinit var cardPolice: CardView
    private lateinit var cardAmbulance: CardView

    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "SeniorCareApp"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_emergency_contact, container, false)

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        // Find Views
        layoutContainer = view.findViewById(R.id.layoutContactsContainer)
        btnAddContact = view.findViewById(R.id.btnAddContact)
        cardPolice = view.findViewById(R.id.cardPolice)
        cardAmbulance = view.findViewById(R.id.cardAmbulance)

        // Load Dynamic Contacts
        loadContacts()

        // --- NEW: Static Button Listeners ---
        cardPolice.setOnClickListener {
            dialNumber("100")
        }

        cardAmbulance.setOnClickListener {
            dialNumber("102")
        }

        btnAddContact.setOnClickListener { showAddContactDialog() }

        return view
    }

    // --- Helper to Dial Number ---
    private fun dialNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun getUserKey(key: String): String {
        val currentUser = prefs.getString("CURRENT_USER", "") ?: ""
        return "${currentUser}_$key"
    }

    private fun loadContacts() {
        layoutContainer.removeAllViews()
        val contactCount = prefs.getInt(getUserKey("CONTACT_COUNT"), 0)

        for (i in 0 until contactCount) {
            val name = prefs.getString(getUserKey("CONTACT_${i}_NAME"), "")
            val number = prefs.getString(getUserKey("CONTACT_${i}_NUMBER"), "")
            if (!name.isNullOrEmpty()) {
                addContactView(name, number ?: "", i)
            }
        }
    }

    private fun addContactView(name: String, number: String, index: Int) {
        val contactView = layoutInflater.inflate(R.layout.item_contact, null)

        val tvName = contactView.findViewById<TextView>(R.id.tvContactName)
        val tvNumber = contactView.findViewById<TextView>(R.id.tvContactNumber)
        val btnDelete = contactView.findViewById<ImageButton>(R.id.btnDeleteContact)
        val layoutRow = contactView.findViewById<LinearLayout>(R.id.layoutContactRow) // Ensure ID in item_contact.xml if you want row click

        tvName.text = name
        tvNumber.text = number

        // Allow clicking the row to call
        contactView.setOnClickListener {
            dialNumber(number)
        }

        btnDelete.setOnClickListener {
            deleteContact(index)
        }

        layoutContainer.addView(contactView)
    }

    private fun showAddContactDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_contacts, null)

        val etName = dialogView.findViewById<EditText>(R.id.etContactName)
        val etNumber = dialogView.findViewById<EditText>(R.id.etContactNumber)

        builder.setView(dialogView)
            .setTitle(R.string.add_new_contact) // Uses translated string
            .setPositiveButton(R.string.save) { _, _ ->
                val name = etName.text.toString()
                val number = etNumber.text.toString()
                if (name.isNotEmpty() && number.isNotEmpty()) {
                    saveContact(name, number)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun saveContact(name: String, number: String) {
        val currentCount = prefs.getInt(getUserKey("CONTACT_COUNT"), 0)

        with(prefs.edit()) {
            putString(getUserKey("CONTACT_${currentCount}_NAME"), name)
            putString(getUserKey("CONTACT_${currentCount}_NUMBER"), number)
            putInt(getUserKey("CONTACT_COUNT"), currentCount + 1)
            apply()
        }
        loadContacts()
    }

    private fun deleteContact(indexToDelete: Int) {
        val currentCount = prefs.getInt(getUserKey("CONTACT_COUNT"), 0)
        val contactsToKeep = ArrayList<Pair<String, String>>()

        for (i in 0 until currentCount) {
            if (i != indexToDelete) {
                val name = prefs.getString(getUserKey("CONTACT_${i}_NAME"), "")
                val number = prefs.getString(getUserKey("CONTACT_${i}_NUMBER"), "")
                if (!name.isNullOrEmpty()) {
                    contactsToKeep.add(Pair(name, number ?: ""))
                }
            }
        }

        val editor = prefs.edit()
        for (i in 0 until currentCount) {
            editor.remove(getUserKey("CONTACT_${i}_NAME"))
            editor.remove(getUserKey("CONTACT_${i}_NUMBER"))
        }

        editor.putInt(getUserKey("CONTACT_COUNT"), contactsToKeep.size)
        for (i in contactsToKeep.indices) {
            editor.putString(getUserKey("CONTACT_${i}_NAME"), contactsToKeep[i].first)
            editor.putString(getUserKey("CONTACT_${i}_NUMBER"), contactsToKeep[i].second)
        }
        editor.apply()
        loadContacts()
    }
}