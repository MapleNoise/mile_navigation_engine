package com.mile.mile_navigation_engine.model

import com.mile.mile_navigation_engine.Navigation.ApplicationRunner

/**
Created by Corentin Houdayer
Dev profile @ https://github.com/houdayec
 **/

data class Gooddeal(
    val id: String,
    val arrayName : Map<LanguageCode, String>,
    val arrayDescription : Map<LanguageCode, String>,
    var arrayWebsiteURL : Map<LanguageCode, String>,
    val imageURL : String?,
    val phoneNumber : String?,
    val status : Long?
    ){
    val name : String
        get() {
            return arrayName[ApplicationRunner.appLanguage].toString()
        }

    val description : String
        get() {
            return arrayDescription[ApplicationRunner.appLanguage].toString()
        }

    val websiteURL : String
        get() {
            return arrayWebsiteURL[ApplicationRunner.appLanguage].toString()
        }
}

