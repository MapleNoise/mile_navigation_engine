package com.mile.mile_navigation_engine.Navigation.model

import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.model.LanguageCode


class Riddle {
    var index: Long = 0
    lateinit var arrayInstructions: HashMap<LanguageCode, String>
    lateinit var arrayQuestions: HashMap<LanguageCode, String>
    lateinit var arrayAnswers: HashMap<LanguageCode, String>
    lateinit var arrayHints: HashMap<LanguageCode, String>
    lateinit var qrCodrUrl: String

    val question : String
        get() {
            return arrayQuestions[ApplicationRunner.instance.appLanguage].toString()
        }

    val instruction : String
        get() {
            return arrayInstructions[ApplicationRunner.instance.appLanguage].toString()
        }

    val answer : String
        get() {
            return arrayAnswers[ApplicationRunner.instance.appLanguage].toString()
        }

    val hint : String
        get() {
            return arrayHints[ApplicationRunner.instance.appLanguage].toString()
        }
}