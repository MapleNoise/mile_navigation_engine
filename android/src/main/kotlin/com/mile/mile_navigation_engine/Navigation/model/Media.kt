package com.mile.mile_navigation_engine.model

data class Media(
    var type: String? = null,
    var url: String? = null,
    var posterURL: String? = null
){
    enum class Type{
        IMAGE, VIDEO;

        companion object{
            fun getTypeFromString(typeString : String) : Type{
                when(typeString.toLowerCase()){
                    "image" -> return IMAGE
                    "video" -> return VIDEO
                    else -> return IMAGE
                }
            }
        }
    }
}