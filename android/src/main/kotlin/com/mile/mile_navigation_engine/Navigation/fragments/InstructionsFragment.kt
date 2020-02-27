package com.mile.miramas.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.CodeScanner
import com.mile.mile_navigation_engine.Navigation.interfaces.OnFragmentInteraction
import com.mile.mile_navigation_engine.R
import com.mile.mile_navigation_engine.model.POI
import com.mile.miramas.fragments.navigation.InstructionsAdapter
import kotlinx.android.synthetic.main.fragment_instructions.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InstructionsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InstructionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InstructionsFragment : Fragment() {

    private lateinit var listenerInteraction: OnFragmentInteraction
    private lateinit var listPois: ArrayList<POI>
    private var currentIndexRiddle: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupRecyclerView()

        close_button.setOnClickListener {
            listenerInteraction.close()
        }
    }

    private fun setupRecyclerView(){
        instructions_recycler_view.apply {
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val instructionsAdapter = InstructionsAdapter(listPois, currentIndexRiddle, context)
            adapter = instructionsAdapter
        }
        instructions_recycler_view.smoothScrollToPosition(currentIndexRiddle.toInt())
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
        fun newInstance(listenerInteraction: OnFragmentInteraction, listPois: ArrayList<POI>, currentIndex: Long) : InstructionsFragment{
            var fragment = InstructionsFragment()
            fragment.listenerInteraction = listenerInteraction
            fragment.listPois = listPois
            fragment.currentIndexRiddle = currentIndex
            return fragment
        }

        val FRAGMENT_TAG: String = BuildConfig.APPLICATION_ID + ".QrCodeFragment_Tag"
    }
}
