package com.mile.mile_navigation_engine.model

import com.mile.mile_navigation_engine.Navigation.ApplicationRunner

/**
Created by Corentin Houdayer on 2019-08-22
Dev profile @ https://github.com/houdayec
 **/

/**
 * Model for hotel
 */
data class Hotel (
    val id : String,
    val idDestination: String?,
    val arrayName : Map<LanguageCode, String>,
    val arrayNameVocal : Map<LanguageCode, String>,
    val arrayDescription : Map<LanguageCode, String>,
    val gooddeals: List<String>,
    val latitude : Double?,
    val longitude : Double?,
    val imageURL : String?,
    val logoURL : String?,
    var arrayWebsiteURL : Map<LanguageCode, String>
){
    val name : String
        get() {
            return arrayName[ApplicationRunner.appLanguage].toString()
        }

    val nameVocal : String
        get() {
            return arrayNameVocal[ApplicationRunner.appLanguage].toString()
        }

    val description : String
        get() {
            return arrayDescription[ApplicationRunner.appLanguage].toString()
        }

    val websiteURL : String
        get() {
            return arrayWebsiteURL[ApplicationRunner.appLanguage].toString()
        }

    var arrayGoodeals = ArrayList<Gooddeal>()
}

