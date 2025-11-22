package com.example.sahayak

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sahayak.R
import java.util.Locale

class ListenWisdomFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var btnSpeakGeetha: Button
    private lateinit var btnSpeakVedas: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listen_wisdom, container, false)
        tts = TextToSpeech(requireContext(), this)

        btnSpeakGeetha = view.findViewById(R.id.btnSpeakGeetha)
        btnSpeakVedas = view.findViewById(R.id.btnSpeakVedas)

        btnSpeakGeetha.setOnClickListener {
            speakOut(getString(R.string.gita_wisdom))
        }
        btnSpeakVedas.setOnClickListener {
            speakOut(getString(R.string.veda_wisdom))
        }
        return view
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(activity, getString(R.string.tts_lang_not_supported), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(activity, getString(R.string.tts_init_failed), Toast.LENGTH_LONG).show()
        }
    }

    private fun speakOut(text: String) {
        if (::tts.isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}