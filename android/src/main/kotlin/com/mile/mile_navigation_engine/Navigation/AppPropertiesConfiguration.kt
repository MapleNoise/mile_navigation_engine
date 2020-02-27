package com.mile.mile_navigation_engine.Navigation

import com.mile.mile_navigation_engine.R
import com.mile.mile_navigation_engine.model.LanguageCode
import java.util.ArrayList

object AppPropertiesConfiguration {

    var idCity = "V0"

    /**
     * BREEZOMETER
     */
    var isBreezometerFeatureActivated = true
    var isAllergensFeatureActivated = true

    /**
     * ROUTE
     */
    var isRouteListFiltersFeatureActivated = true

    /**
     * LOGIN - SSO
     */
    var isDecathlonLoginActivated = true

    /**
     * PICTURE SHARING
     */
    var isPictureSharingFeatureActivated = true

    /**
     * NOTIFICATIONS
     */
    var isGeofenceNotificationFeatureActivated = true

    /**
     * LOGIN
     */
    var isLoginAnonymousOnly = true

    var idRoutesMightBeClosed: MutableList<String>
    var idRoutesManualNavigation: MutableList<String>
    var arrayAudioTreasureHuntIntro = hashMapOf<LanguageCode, Int>(
        LanguageCode.FR to R.raw.treasure_hunt_introduction,
        LanguageCode.EN to R.raw.treasure_hunt_introduction_en
    )
    var arrayAudioTreasureHuntEnd = hashMapOf<LanguageCode, Int>(
        LanguageCode.FR to R.raw.treasure_hunt_end,
        LanguageCode.EN to R.raw.treasure_hunt_end_en
    )
    var arrayAudioTreasureHuntSecretArea = hashMapOf<LanguageCode, Int>(
        LanguageCode.FR to R.raw.treasure_hunt_secret_area,
        LanguageCode.EN to R.raw.treasure_hunt_secret_area_en
    )

    init {
        idRoutesMightBeClosed = ArrayList()
        idRoutesMightBeClosed.add("PA15")

        idRoutesManualNavigation = ArrayList()
        idRoutesManualNavigation.add("PA15")
    }

}
