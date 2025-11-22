package com.example.sahayak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class TalkieReadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_talkie_read, container, false)

        val tvStory: TextView = view.findViewById(R.id.tvStoryText)
        val btnRamayan: Button = view.findViewById(R.id.btnRamayan)
        val btnMahabharat: Button = view.findViewById(R.id.btnMahabharat)

        btnRamayan.setOnClickListener {
            tvStory.text = getString(R.string.story_ram_text)
        }
        btnMahabharat.setOnClickListener {
            tvStory.text = getString(R.string.story_mahabharath_text)
        }
        return view
    }
}