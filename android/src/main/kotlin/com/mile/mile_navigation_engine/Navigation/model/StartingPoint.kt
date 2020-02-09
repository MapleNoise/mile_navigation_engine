package com.mile.mile_navigation_engine.model

import android.location.Location

data class StartingPoint(
    var title: String? = null,
    var lat: Double? = null,
    var long: Double? = null,
    var index: Int? = null,
    var pos: Int = 0,
    var posPOI: Int = 0
){
    fun getLocation(): Location {
        val location = Location("provider")
        location.latitude = lat!!
        location.longitude = long!!
        return location
    }
}