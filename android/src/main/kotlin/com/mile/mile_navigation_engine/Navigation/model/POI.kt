package com.mile.mile_navigation_engine.model

import android.location.Location
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.Navigation.model.Riddle
import com.mile.mile_navigation_engine.R

data class POI(
        val id : String,
        var arrayName : Map<LanguageCode, String>,
        var arrayDescription : Map<LanguageCode, String>,
        var arrayMP3 : Map<LanguageCode, String>,
        var arrayTextMP3 : Map<LanguageCode, String>,
        var arrayCoordinates : List<Location>,
        val isMP3Activated : Boolean?,
        val imageURL : String?,
        val videoURL: String?,
        val posterURL: String?,
        var isSeen : Boolean,
        val doesBlockMapboxSpeaker : Boolean?,
        val radius : Long,
        var type : Type,
        val idDirection : Long?,
        val isAutoDirectionAcivated : Boolean,
        val status : Long,
        val riddle: Riddle,
        val urlWebView : String,
        val modules : List<Module>
){
    enum class Type(val id : Long) {
        UNDEFINED(0),
        MONUMENT(1),
        CULTURE(2),
        ENTERTAINMENT(3),
        MERCHANT(4),
        STREET_PLACE_NEIGHBOURHOOD(5),
        RELIGION(6),
        NATURE(7),
        WATER(8),
        DIRECTIONNAL_CLOCKWISE(9),
        DIRECTIONNAL_COUNTERCLOCKWISE(10),
        NO_ANNOTATION(11),
        THEME(12),
        CHALLENGE(13);

        companion object{
            fun get(id : Long) : Type {
                values().forEach {
                    if(id == it.ordinal.toLong())
                        return it
                }
                return UNDEFINED
            }

            fun getDrawableID(id : Long) : Int{
                var drawableID = 0
                when(id.toInt()){
                    0 -> drawableID = R.drawable.pin_undefined
                    1 -> drawableID = R.drawable.pin_monument
                    2 -> drawableID = R.drawable.pin_culture
                    3 -> drawableID = R.drawable.pin_loisir
                    4 -> drawableID = R.drawable.pin_marchand
                    5 -> drawableID = R.drawable.pin_quartier
                    6 -> drawableID = R.drawable.pin_religion
                    7 -> drawableID = R.drawable.pin_nature
                    8 -> drawableID = R.drawable.pin_eau
                    11 -> drawableID = R.drawable.pin_undefined
                    12 -> drawableID = R.drawable.pin_thematique
                    13 -> drawableID = R.drawable.pin_undefined // TODO add sport icon + color icons
                    else -> drawableID = R.drawable.pin_undefined
                }
                return drawableID
            }
        }

    }

    class Direction(val id : Long){
        companion object{
            fun getDrawableID(id : Long?) : Int{
                var drawableID : Int
                when(id?.toInt()){
                    1 -> drawableID = R.drawable.go_straight
                    2 -> drawableID = R.drawable.ic_maneuver_merge_left
                    3 -> drawableID = R.drawable.ic_maneuver_merge_right
                    4 -> drawableID = R.drawable.ic_maneuver_turn_45_left
                    5 -> drawableID = R.drawable.ic_maneuver_turn_45
                    6 -> drawableID = R.drawable.ic_maneuver_turn_75_left
                    7 -> drawableID = R.drawable.ic_maneuver_turn_75
                    8 -> drawableID = R.drawable.ic_maneuver_turn_180
                    9 -> drawableID = R.drawable.ic_maneuver_turn_180
                    10 -> drawableID = R.drawable.ic_maneuver_roundabout_straight
                    11 -> drawableID = R.drawable.ic_maneuver_roundabout_sharp_left
                    12 -> drawableID = R.drawable.ic_maneuver_roundabout_sharp_right
                    13 -> drawableID = R.drawable.ic_maneuver_roundabout_left
                    14 -> drawableID = R.drawable.ic_maneuver_roundabout_right
                    15 -> drawableID = R.drawable.ic_maneuver_roundabout_slight_left
                    16 -> drawableID = R.drawable.ic_maneuver_roundabout_slight_right
                    else -> drawableID = R.drawable.go_straight
                }
                return drawableID
            }
        }
    }

    val name : String
        get() {
            return arrayName[ApplicationRunner.appLanguage].toString()
        }

    val description : String
        get() {
            return arrayDescription[ApplicationRunner.appLanguage].toString()
        }

    val mp3 : String
        get() {
            return arrayMP3[ApplicationRunner.appLanguage].toString()
        }

    val mp3Text : String
        get() {
            return arrayTextMP3[ApplicationRunner.appLanguage].toString()
        }

    val mainCoordinates : Location
        get() {
            return arrayCoordinates.first()
        }

    val mp3FileString : String
        get() {
            var mp3Name = mp3
            var temp = mp3.substring(mp3Name.indexOf("%2F")+3)
            var final = temp.substring(0, temp.indexOf(".mp3"))
            return final.toLowerCase()
        }
}


