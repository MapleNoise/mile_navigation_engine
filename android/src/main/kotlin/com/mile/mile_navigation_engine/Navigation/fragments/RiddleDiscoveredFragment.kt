package com.mile.miramas.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.mile.mile_navigation_engine.Navigation.interfaces.OnFragmentInteraction
import com.mile.mile_navigation_engine.R
import kotlinx.android.synthetic.main.fragment_introduction_treasure_hunt.*

class RiddleDiscoveredFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var listenerInteraction: OnFragmentInteraction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riddle_discovered, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button_start.setOnClickListener {
            listenerInteraction.close()
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(listenerInteraction: OnFragmentInteraction) : RiddleDiscoveredFragment{
            var fragment = RiddleDiscoveredFragment()
            fragment.listenerInteraction = listenerInteraction
            return fragment
        }

        val FRAGMENT_TAG: String = BuildConfig.APPLICATION_ID + ".RiddleDiscoveredFragment_Tag"
    }
}
