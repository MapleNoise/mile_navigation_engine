package com.mile.mile_navigation_engine.model

import android.graphics.drawable.Drawable
import com.mile.mile_navigation_engine.R
import kotlinx.android.synthetic.main.dialog_report_problem.view.*

class Module{

    var title : String? = null
    var subtitle : String? = null
    var description : String? = null
    var label: String? = null
    var thumbnail : String? = null
    var country : String ? = null
    var videoURL : String ? = null
    var posterURL : String ? = null
    var url : String? = null
    var gltfURL : String? = null
    var usdzURL : String? = null
    var type : ModuleType? = null
    var elements : List<Module>? = null
    var rescueAR : Module? = null
    var address : String? = null
    var imageAURL : String? = null
    var imageBURL : String? = null
    var lat: Double? = null
    var long: Double? = null
    var index: Long = 0
    var instruction: String? = null
    var instructionsAR: List<Module>? = null
    var positionAR: HashMap<String, Long>? = null
    var rotationAR: HashMap<String, Long>? = null
    var timestampAR: Long? = null

    enum class ModuleType{
        AR, VR, WEB, MORE, LINK, ADDRESS, SEARCH, SLIDER;

        companion object{
            fun getTypeFromString(typeString : String) : ModuleType{
                when(typeString.toLowerCase()){
                    "ar" -> return AR
                    "vr" -> return VR
                    "web" -> return WEB
                    "more" -> return MORE
                    "link" -> return LINK
                    "address" -> return ADDRESS
                    "search" -> return SEARCH
                    "slider" -> return SLIDER
                    else -> return WEB
                }
            }
        }

        fun getIconFromType() : Int {
            when(this){
                LINK -> return R.drawable.ic_open_in_new
                SEARCH -> return R.drawable.ic_open_in_new
                ADDRESS -> return R.drawable.ic_map_marker_radius
                else -> return R.drawable.ic_open_in_new
            }
        }
    }
}