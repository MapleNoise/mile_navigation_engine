package com.mile.miramas.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import com.budiyev.android.codescanner.CodeScannerView
import com.mile.mile_navigation_engine.Navigation.interfaces.OnFragmentInteraction
import com.mile.mile_navigation_engine.R
import com.mile.mile_navigation_engine.model.POI
import com.mile.mile_navigation_engine.utils.AppDataHolder
import com.mile.mile_navigation_engine.utils.PhoneUtils
import kotlinx.android.synthetic.main.fragment_riddle.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RiddleFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RiddleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RiddleFragment : Fragment() {

    private lateinit var poi: POI
    private var numberOfAnswerTries = 0
    var MAX_NUMBER_OF_TRIES = 2
    var NUMBER_OF_TRIES_GET_HINT = 1
    lateinit var listenerInteraction: OnFragmentInteraction
    lateinit var listenerAnswer: AnswerListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riddle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        bindView()
        initListeners()
    }

    private fun bindView(){
        title_riddle.text = String.format(getString(R.string.title_riddle_indexed), poi.riddle.index, AppDataHolder.currentRoute?.arrayPois?.size)
        riddle_question.text = poi.riddle.question
        hint.text = poi.riddle.hint
        showHintContainer(false)
    }

    private fun initListeners(){
        button_validate_answer.setOnClickListener {
            checkAnswer()
            removePhoneKeypad()
        }

        button_see_hint.setOnClickListener {
            showHint(true)
        }

        button_continue.setOnClickListener {
            listenerInteraction.close()
        }

        text_input_answer.setOnClickListener {
            text_input_answer.setBackgroundColor(resources.getColor(R.color.white))
        }
    }

    private fun checkAnswer(){
        if(editext_answer.text.toString().toLowerCase().equals(poi.riddle.answer.toLowerCase())){ // Checking if answer is correct
            //rightAnswer()
            showSuccessContainer(true)
            listenerAnswer.answered(true)
        }else{
            numberOfAnswerTries++
            if(numberOfAnswerTries == NUMBER_OF_TRIES_GET_HINT){
                showHintContainer(true)
                wrongAnswer()
                PhoneUtils.triggerVibration(500)
            }else if(numberOfAnswerTries >= MAX_NUMBER_OF_TRIES){
                wrongAnswer()
                listenerAnswer.answered(false)
            }
        }
    }

    private fun wrongAnswer(){
        if(numberOfAnswerTries == MAX_NUMBER_OF_TRIES){
            image_success_container.setImageDrawable(resources.getDrawable(R.drawable.image_sad))
            text_success_container.text = getString(R.string.text_fail_riddle)
            showSuccessContainer(true)
        }else{
            //text_input_answer.boxStrokeColor = resources.getColor(R.color.route_closed)
            text_input_answer.setBackgroundColor(resources.getColor(R.color.route_closed))
            editext_answer.text.clear()
        }

    }

    private fun rightAnswer(){
        text_input_answer.setBackgroundColor(resources.getColor(R.color.route_open))
        editext_answer.text.clear()
        editext_answer.text.append(getString(R.string.label_good_answer))
    }

    private fun resetAnswer(){
        text_input_answer.setBackgroundColor(resources.getColor(R.color.white))
    }

    /**
     * Managing components visibility
     */

    private fun showHintContainer(show: Boolean){
        hint_container.isVisible = show
    }

    private fun showHint(show: Boolean){
        if(show){
            button_see_hint.visibility = View.GONE
            hint.visibility = View.VISIBLE
        }else{
            button_see_hint.visibility = View.VISIBLE
            hint.visibility = View.GONE
        }
    }

    private fun showSuccessContainer(show: Boolean){
        riddle_container.isVisible = !show
        success_container.isVisible = show
        answer_success_container.text = poi.riddle.answer
    }

    fun removePhoneKeypad() {
        val inputManager: InputMethodManager = view
            ?.getContext()
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val binder = view!!.windowToken
        inputManager.hideSoftInputFromWindow(
            binder,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    interface AnswerListener{
        fun answered(right: Boolean)
    }

    companion object {

        @JvmStatic
        fun newInstance(poi: POI, listenerInteraction: OnFragmentInteraction, answerListener: AnswerListener) : RiddleFragment{
            var fragment = RiddleFragment()
            fragment.poi = poi
            fragment.listenerInteraction = listenerInteraction
            fragment.listenerAnswer = answerListener
            return fragment
        }

        val FRAGMENT_TAG: String = BuildConfig.APPLICATION_ID + ".RiddleFragment_Tag"
    }
}
