package com.mile.mile_navigation_engine.model

import android.graphics.drawable.Drawable
import android.location.Location
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.R


/**
Created by Corentin Houdayer on 2019-08-23
Dev profile @ https://github.com/houdayec
 **/

data class RouteDataClass(
        val id : String,
        val idDestination : String?,
        var arrayName : Map<LanguageCode, String>?,
        var arrayDescription : Map<LanguageCode, String>?,
        var arrayCoordinates : List<Location>?,
        var previewCoordinates: List<Location>?,
        val pois : List<String>?,
        val ascent : Long?,
        val length : Long?,
        val status : Long?,
        val imageURL : String?,
        val listMedias: List<Media>?,
        val averageDuration : Long?,
        var methodLocomotion : Locomotion?,
        val globalAQI : Long?,
        val allergensAverage : Long?,
        val arrayShareURL : Map<LanguageCode, String>?,
        val isNavigationActivated : Boolean?,
        val areSmartFactsEnabled : Boolean?,
        val isStartingPointStatic : Boolean?,
        val arrayStartingPoints: ArrayList<StartingPoint>?,
        val isSynthActivated : Boolean?,
        val isLoop : Boolean?
) {
    enum class Locomotion(val id : Long) {
        WALKING(1),
        RUNNING(2),
        BICYCLE(3);

        fun getDirectionCriteria() : String{
            when(this.id.toInt()){
                1 -> return DirectionsCriteria.PROFILE_CYCLING
                2 -> return DirectionsCriteria.PROFILE_CYCLING
                3 -> return DirectionsCriteria.PROFILE_CYCLING
                else -> return DirectionsCriteria.PROFILE_CYCLING
            }
        }

        fun getIcon() : Drawable? {
            when(this.id.toInt()){
                1 -> return ApplicationRunner.instance.getDrawable(R.drawable.ic_shoe_print)
                2 -> return ApplicationRunner.instance.getDrawable(R.drawable.ic_run)
                3 -> return ApplicationRunner.instance.getDrawable(R.drawable.ic_bike)
                else -> return ApplicationRunner.instance.getDrawable(R.drawable.ic_shoe_print)
            }
        }

        companion object {
            fun getLocomotion(id: Long) : Locomotion {
                when(id.toInt()){
                    1 -> return WALKING
                    2 -> return RUNNING
                    3 -> return BICYCLE
                    else -> return WALKING
                }
            }
        }

    }
    val name : String
        get() {
            return arrayName!![ApplicationRunner.appLanguage].toString()
        }

    val description : String
        get() {
            return arrayDescription!![ApplicationRunner.appLanguage].toString()
        }

    val shareURL : String
        get() {
            return arrayShareURL!![ApplicationRunner.appLanguage].toString()
        }

    val hasStartingPoints : Boolean
        get() {
            return this.arrayStartingPoints != null && this.arrayStartingPoints?.size!! > 0
        }

    var arrayPois = ArrayList<POI>()

    companion object{
        fun getLabelForAirQuality(level: Int?) : String{
            if(level == null) return ApplicationRunner.instance.getString(R.string.label_unknown)
            if(level > 66)
                return ApplicationRunner.instance.getString(R.string.label_good)
            else if(level > 33)
                return ApplicationRunner.instance.getString(R.string.label_medium)
            else
                return ApplicationRunner.instance.getString(R.string.label_bad)
        }

        fun getLabelForAllergensQuality(level: Int?) : String{
            if(level == null) return ApplicationRunner.instance.getString(R.string.label_unknown)
            if(level > 66)
                return ApplicationRunner.instance.getString(R.string.label_bad)
            else if(level > 33)
                return ApplicationRunner.instance.getString(R.string.label_medium)
            else
                return ApplicationRunner.instance.getString(R.string.label_good)
        }
    }
}
