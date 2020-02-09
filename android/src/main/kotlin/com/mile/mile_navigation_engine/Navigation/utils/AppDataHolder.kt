package com.mile.mile_navigation_engine.utils

import com.mile.mile_navigation_engine.model.*
import io.flutter.app.FlutterApplication

/**
Created by Corentin Houdayer on 2019-08-22
Dev profile @ https://github.com/houdayec
 **/

// Class designed to keep alive data across the app to avoir passing data through bundles, extras or other ways
// which are memory consumers, need to implement some interface as parcelable... and reduce app performances
// Objects are light and easy to fetch/pass
class AppDataHolder {

    companion object {
        var currentHotel : Hotel? = null
        var currentRoute : RouteDataClass? = null
        var currentPOI : POI? = null
        var currentGooddeal : Gooddeal? = null
        var applicationInstance : FlutterApplication? = null
        var currentStartingPoint: StartingPoint? = null

        var gpsColor : String? = "#000000"
    }

}