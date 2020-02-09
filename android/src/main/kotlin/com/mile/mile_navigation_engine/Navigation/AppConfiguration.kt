package com.mile.mile_navigation_engine.Navigation

class AppConfiguration {

    companion object{

        /**
         * FEATURES MANAGER
         */
        val experience_feature = false
        var isSingleCity = true
        var breezometerDataDisplayed = false
        var multiLanguageActivated = false


        /**
         * WHITE LABEL CONF
         */
        val currentCityID = "V2"
        val currentDestination = "V2"

        val BASE_URL_PROJECT = ""
    }

    class Navigation {
        companion object {
            var useCustomAudioDescriptionForPOI = false
            var coordinatesFromMapper = true
            var debugMode = false
        }
    }
}