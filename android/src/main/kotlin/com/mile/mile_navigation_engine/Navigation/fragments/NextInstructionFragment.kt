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
import com.mile.mile_navigation_engine.model.POI
import kotlinx.android.synthetic.main.fragment_next_instruction.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NextInstructionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NextInstructionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NextInstructionFragment : Fragment() {

    private lateinit var listenerInteraction: OnFragmentInteraction
    private lateinit var poi: POI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_next_instruction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        instruction_text.text = poi.riddle.instruction

        confirm_button.setOnClickListener {
            listenerInteraction.close()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(listenerInteraction: OnFragmentInteraction, poi: POI) : NextInstructionFragment{
            var fragment = NextInstructionFragment()
            fragment.listenerInteraction = listenerInteraction
            fragment.poi = poi
            return fragment
        }

        val FRAGMENT_TAG: String = BuildConfig.APPLICATION_ID + ".QrCodeFragment_Tag"
    }
}
