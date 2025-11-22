package com.example.sahayak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.sahayak.R

class TalkieFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_talkie, container, false)

        val btnRead: Button = view.findViewById(R.id.btnReadStories)
        val btnListen: Button = view.findViewById(R.id.btnListenWisdom)

        btnRead.setOnClickListener {
            (activity as MainActivity).showFragment(TalkieReadFragment())
        }
        btnListen.setOnClickListener {
            (activity as MainActivity).showFragment(ListenWisdomFragment())
        }
        return view
    }
}