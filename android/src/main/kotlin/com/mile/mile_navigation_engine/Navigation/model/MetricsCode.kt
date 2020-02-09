package com.mile.mile_navigation_engine.model

import com.mapbox.api.directions.v5.DirectionsCriteria

/**
Created by Corentin Houdayer on 2019-09-06
Dev profile @ https://github.com/houdayec
 **/

enum class MetricsCode {
    METRIC{
        override fun toString(): String {
            return DirectionsCriteria.METRIC
        }
    },
    IMPERIAL{
        override fun toString(): String {
            return DirectionsCriteria.IMPERIAL
        }
    },
    SPEED_KMH{
        override fun toString(): String {
            return "km/h"
        }
    },
    SPEED_MPH{
        override fun toString(): String {
            return "mi/h"
        }
    },
    METERS{
        override fun toString(): String {
            return "km"
        }
    },
    DISTANCE_KMETERS{
        override fun toString(): String {
            return "km"
        }
    },
    DISTANCE_METERS{
        override fun toString(): String {
            return "m"
        }
    },
    DISTANCE_MILE{
        override fun toString(): String {
            return "mi"
        }
    },
    DISTANCE_FEET{
        override fun toString(): String {
            return "ft"
        }
    },
    TIME_HOUR{
        override fun toString(): String {
            return "h"
        }
    },
    TIME_MINUTE{
        override fun toString(): String {
            return "mn"
        }
    },
    TIME_SECOND{
        override fun toString(): String {
            return "s"
        }
    };

    companion object{
        fun getDistanceMetricFromLanguageCode(languageCode: LanguageCode) : MetricsCode{
            when(languageCode){
                LanguageCode.FR -> return METRIC
                LanguageCode.EN -> return IMPERIAL
                else -> return METRIC
            }
        }

        fun getSpeedMetricFromLanguageCode(languageCode: LanguageCode) : MetricsCode{
            when(languageCode){
                LanguageCode.FR -> return SPEED_KMH
                LanguageCode.EN -> return SPEED_MPH
                else -> return SPEED_KMH
            }
        }
    }
}