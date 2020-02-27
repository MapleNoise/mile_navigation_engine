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
import kotlinx.android.synthetic.main.fragment_instructions.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QrCodeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QrCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrCodeFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var listener: OnQrCodeDetected
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
        return inflater.inflate(R.layout.fragment_qr_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                if(it.text.equals(poi.riddle.qrCodrUrl)){
                    listener.onQrCodeUrlFetched(it.text)
                }else{
                    codeScanner.startPreview()
                    Toast.makeText(activity, "This is not the right QRCode, find the one for your current step", Toast.LENGTH_SHORT).show()
                }

            }
        }
        scannerView.setOnClickListener {
            //codeScanner.startPreview()
        }
        codeScanner.startPreview()

        close_button.setOnClickListener {
            listenerInteraction.close()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnQrCodeDetected {
        fun onQrCodeUrlFetched(url: String)
    }

    companion object {

        @JvmStatic
        fun newInstance(listener: OnQrCodeDetected, listenerInteraction: OnFragmentInteraction, poi: POI) : QrCodeFragment{
            var fragment = QrCodeFragment()
            fragment.listener = listener
            fragment.listenerInteraction = listenerInteraction
            fragment.poi = poi
            return fragment
        }

        val FRAGMENT_TAG: String = BuildConfig.APPLICATION_ID + ".QrCodeFragment_Tag"
    }
}
