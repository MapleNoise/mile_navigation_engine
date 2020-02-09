package com.mile.mile_navigation_engine.model

import java.util.*

/**
Created by Corentin Houdayer on 2019-08-21
Dev profile https://github.com/houdayec
 **/

enum class LanguageCode {
    FR{
        override fun toString(): String {
            return "FR"
        }
    },
    EN{
        override fun toString(): String {
            return "EN"
        }
    },
    ES{
        override fun toString(): String {
            return "ES"
        }
    };

    fun getLocaleLanguage() : String {
        when(this){
            FR -> return Locale("fr").language
            EN -> return Locale.ENGLISH.language
            ES -> return Locale("es").language
            else -> return Locale("fr").language
        }
    }

    fun getLocale() : Locale {
        when(this){
            FR -> return Locale("fr")
            EN -> return Locale.ENGLISH
            ES -> return Locale("es")
            else -> return Locale("fr")
        }
    }

     companion object{
         fun getLanguageCodeFromLocale(locale: Locale) : LanguageCode{
             when(locale.displayLanguage.toLowerCase()){
                 "english" -> return EN
                 "français" -> return FR
                 else -> return EN
             }
         }
     }

}