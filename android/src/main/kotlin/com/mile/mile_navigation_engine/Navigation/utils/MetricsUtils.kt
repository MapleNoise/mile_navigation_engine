package com.mile.mile_navigation_engine.utils

import android.util.Log
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.model.MetricsCode
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

/*class MetricsUtils {
    companion object{

        fun displayMillisecondsToSecondsFormatted(time: Int) : String{
            var numberOfSeconds = (time / 1000)
            var numberOfSecondsFormatString: String
            var numberOfMinutesFormatString : String
            var numberOfSecondsFormat = numberOfSeconds % 60
            var numberOfMinutesFormat = (numberOfSeconds-numberOfSecondsFormat)/60

            if(numberOfMinutesFormat < 10)
                numberOfMinutesFormatString = "0$numberOfMinutesFormat"
            else
                numberOfMinutesFormatString = numberOfMinutesFormat.toString()

            if(numberOfSecondsFormat < 10)
                numberOfSecondsFormatString = "0$numberOfSecondsFormat"
            else
                numberOfSecondsFormatString = numberOfSecondsFormat.toString()

            return "$numberOfMinutesFormatString:$numberOfSecondsFormatString"
        }

        fun displayMillisecondsToSecondsFormatted(baseTimestamp: Long, time: Long) : String{
            var numberOfSeconds = (time-baseTimestamp)/1000
            var numberOfSecondsFormatString: String
            var numberOfMinutesFormatString : String
            var numberOfSecondsFormat = numberOfSeconds % 60
            var numberOfMinutesFormat = (numberOfSeconds-numberOfSecondsFormat)/60

            if(numberOfMinutesFormat < 10)
                numberOfMinutesFormatString = "0$numberOfMinutesFormat"
            else
                numberOfMinutesFormatString = numberOfMinutesFormat.toString()

            if(numberOfSecondsFormat < 10)
                numberOfSecondsFormatString = "0$numberOfSecondsFormat"
            else
                numberOfSecondsFormatString = numberOfSecondsFormat.toString()

            return "$numberOfMinutesFormatString:$numberOfSecondsFormatString"
        }

        fun displayMillisecondsToSecondsFormatted(time: Long) : String{
            var numberOfSeconds = time/1000
            var numberOfSecondsFormatString: String
            var numberOfMinutesFormatString : String
            var numberOfSecondsFormat = numberOfSeconds % 60
            var numberOfMinutesFormat = (numberOfSeconds-numberOfSecondsFormat)/60

            if(numberOfMinutesFormat < 10)
                numberOfMinutesFormatString = "0$numberOfMinutesFormat"
            else
                numberOfMinutesFormatString = numberOfMinutesFormat.toString()

            if(numberOfSecondsFormat < 10)
                numberOfSecondsFormatString = "0$numberOfSecondsFormat"
            else
                numberOfSecondsFormatString = numberOfSecondsFormat.toString()

            return "$numberOfMinutesFormatString:$numberOfSecondsFormatString"
        }

        fun displaySpeedFormatted(speed: Float) : String{
            // TODO PER METRICS
            return "${getHumanUnderstandableSpeed(speed)} ${ApplicationRunner.appSpeedMetrics}"
        }

        fun getHumanUnderstandableSpeed(speed: Float): BigDecimal {
            // TODO PER METRICS
            return (speed * 3.6f).toBigDecimal().setScale(1, RoundingMode.HALF_EVEN)
        }

        fun displayDistanceFormatted(distance: Double?) : String{
            if(distance == null) return ""
            val distanceInMeters = distance.roundToInt()
            var distanceInKmString = (distanceInMeters/1000.toFloat()).toString()
            if(ApplicationRunner.appDistanceMetrics == MetricsCode.METRIC){
                if(distanceInMeters >= 100 && distanceInMeters < 1000){
                    if(distanceInKmString.length == 4){
                        distanceInKmString += "0"
                    }
                }
                val arrayDistancesKmAndM = distanceInKmString.split('.')
                val numberKm = arrayDistancesKmAndM[0].toInt()
                val numberM = arrayDistancesKmAndM[1].toInt()
                if(numberKm < 1){
                    return "$numberM ${MetricsCode.DISTANCE_METERS}"
                }else{
                    val distanceMetersRounded = (numberM/10).toBigDecimal().setScale(0, RoundingMode.HALF_EVEN)
                    return "$numberKm,$distanceMetersRounded ${MetricsCode.DISTANCE_KMETERS}"
                }
            }else if(ApplicationRunner.appDistanceMetrics == MetricsCode.IMPERIAL){
                var distanceInFeet = distanceInMeters * 3.281
                var distanceInMilesString = (distanceInFeet/5280.toFloat()).toString()
                if(distanceInFeet >= 100 && distanceInFeet < 1000){
                    if(distanceInMilesString.length == 4){
                        distanceInMilesString += "0"
                    }
                }
                val arrayDistancesMilesAndFeet = distanceInMilesString.split('.')
                val numberMiles = arrayDistancesMilesAndFeet[0].toInt()
                var numberFeet = arrayDistancesMilesAndFeet[1].toLong()
                if(numberFeet > 9){
                    numberFeet = numberFeet.toString().substring(0, 1).toLong()
                }

                return "$numberMiles,$numberFeet ${MetricsCode.DISTANCE_MILE}"

            }else{
                return ""
            }
        }

        fun displayDurationFromDistanceFormatted(distance: Double) : String{
            val distanceInMeters = distance.roundToInt()
            var distanceInKm = (distanceInMeters.toFloat()/1000)
            Log.d("distanceInKm", distanceInKm.toString())
            var duration = (distanceInKm/4)*60
            Log.d("duration", duration.toString())
            var nbHours = 0
            var nbMinutes = 0
            if(duration > 60){
                var restMinutes = duration % 60
                nbHours = ((duration-restMinutes)/60).toInt()
                nbMinutes = restMinutes.toInt()
            }else{
                nbMinutes = duration.toInt()
            }
            if(nbHours < 1){
                return "$nbMinutes ${MetricsCode.TIME_MINUTE}"
            }else{
                return "$nbHours${MetricsCode.TIME_HOUR}$nbMinutes${MetricsCode.TIME_MINUTE}"
            }
        }

        fun displayDuration(durationInMinutes: Long?) : String{
            if (durationInMinutes == null) return ""
            if(durationInMinutes >= 60){
                var hours = (durationInMinutes/60).toInt()
                var minutes = durationInMinutes%60
                if(minutes == 0L)
                    return "$hours${MetricsCode.TIME_HOUR}"
                else
                    return "$hours${MetricsCode.TIME_HOUR}$minutes${MetricsCode.TIME_MINUTE}"
            }else{
                return "$durationInMinutes ${MetricsCode.TIME_MINUTE}"
            }
        }

        fun displayAscentFormatted(distance: Long?) : String{
            if(ApplicationRunner.appDistanceMetrics == MetricsCode.METRIC){
                return "$distance ${MetricsCode.DISTANCE_METERS}"
            }else if(ApplicationRunner.appDistanceMetrics == MetricsCode.IMPERIAL){
                var ascentRounded = (distance?.times(3.281))?.roundToInt()
                return "$ascentRounded ${MetricsCode.DISTANCE_FEET}"
            }else{
                return ""
            }
        }

        fun convertFromMinutesToMs(time: Long): Long{
            return time*60*1000
        }

        fun convertFromSecondsToMs(time: Long): Long{
            return time*1000
        }
    }
}


import android.util.Log
import com.mile.milepositioningengine.ApplicationRunner
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt*/

class MetricsUtils {
    companion object{

        fun displayMillisecondsToSecondsFormatted(time: Int) : String{
            var numberOfSeconds = (time / 1000)
            var numberOfSecondsFormatString: String
            var numberOfMinutesFormatString : String
            var numberOfSecondsFormat = numberOfSeconds % 60
            var numberOfMinutesFormat = (numberOfSeconds-numberOfSecondsFormat)/60

            if(numberOfMinutesFormat < 10)
                numberOfMinutesFormatString = "0$numberOfMinutesFormat"
            else
                numberOfMinutesFormatString = numberOfMinutesFormat.toString()

            if(numberOfSecondsFormat < 10)
                numberOfSecondsFormatString = "0$numberOfSecondsFormat"
            else
                numberOfSecondsFormatString = numberOfSecondsFormat.toString()

            return "$numberOfMinutesFormatString:$numberOfSecondsFormatString"
        }

        fun displayMillisecondsToSecondsFormatted(baseTimestamp: Long, time: Long) : String{
            var numberOfSeconds = (time-baseTimestamp)/1000
            var numberOfSecondsFormatString: String
            var numberOfMinutesFormatString : String
            var numberOfSecondsFormat = numberOfSeconds % 60
            var numberOfMinutesFormat = (numberOfSeconds-numberOfSecondsFormat)/60

            if(numberOfMinutesFormat < 10)
                numberOfMinutesFormatString = "0$numberOfMinutesFormat"
            else
                numberOfMinutesFormatString = numberOfMinutesFormat.toString()

            if(numberOfSecondsFormat < 10)
                numberOfSecondsFormatString = "0$numberOfSecondsFormat"
            else
                numberOfSecondsFormatString = numberOfSecondsFormat.toString()

            return "$numberOfMinutesFormatString:$numberOfSecondsFormatString"
        }

        fun displayMillisecondsToSecondsFormatted(time: Long) : String{
            var numberOfSeconds = time/1000
            var numberOfSecondsFormatString: String
            var numberOfMinutesFormatString : String
            var numberOfSecondsFormat = numberOfSeconds % 60
            var numberOfMinutesFormat = (numberOfSeconds-numberOfSecondsFormat)/60

            if(numberOfMinutesFormat < 10)
                numberOfMinutesFormatString = "0$numberOfMinutesFormat"
            else
                numberOfMinutesFormatString = numberOfMinutesFormat.toString()

            if(numberOfSecondsFormat < 10)
                numberOfSecondsFormatString = "0$numberOfSecondsFormat"
            else
                numberOfSecondsFormatString = numberOfSecondsFormat.toString()

            return "$numberOfMinutesFormatString:$numberOfSecondsFormatString"
        }

        fun displaySpeedFormatted(speed: Float) : String{
            // TODO PER METRICS
            return "${getHumanUnderstandableSpeed(speed)} ${ApplicationRunner.appSpeedMetrics}"
        }

        fun getHumanUnderstandableSpeed(speed: Float): BigDecimal {
            // TODO PER METRICS
            if(!speed.isNaN())
                return (speed * 3.6f).toBigDecimal().setScale(1, RoundingMode.HALF_EVEN)
            else return 0.toBigDecimal()
        }

        fun displayDistanceFormatted(distance: Double?) : String{
            if(distance == null) return ""
            val distanceInMeters = distance.roundToInt()
            var distanceInKmString = (distanceInMeters/1000.toFloat()).toString()
            if(ApplicationRunner.appDistanceMetrics == MetricsCode.METRIC){
                if(distanceInMeters >= 100 && distanceInMeters < 1000){
                    if(distanceInKmString.length == 4){
                        distanceInKmString += "0"
                    }
                }
                val arrayDistancesKmAndM = distanceInKmString.split('.')
                val numberKm = arrayDistancesKmAndM[0].toInt()
                val numberM = arrayDistancesKmAndM[1].toInt()
                if(numberKm < 1){
                    return "$numberM ${MetricsCode.DISTANCE_METERS}"
                }else{
                    val distanceMetersRounded = (numberM/10).toBigDecimal().setScale(0, RoundingMode.HALF_EVEN)
                    return "$numberKm,$distanceMetersRounded ${MetricsCode.DISTANCE_KMETERS}"
                }
            }else if(ApplicationRunner.appDistanceMetrics == MetricsCode.IMPERIAL){
                var distanceInFeet = distanceInMeters * 3.281
                var distanceInMilesString = (distanceInFeet/5280.toFloat()).toString()
                if(distanceInFeet >= 100 && distanceInFeet < 1000){
                    if(distanceInMilesString.length == 4){
                        distanceInMilesString += "0"
                    }
                }
                val arrayDistancesMilesAndFeet = distanceInMilesString.split('.')
                val numberMiles = arrayDistancesMilesAndFeet[0].toInt()
                var numberFeet = arrayDistancesMilesAndFeet[1].toLong()
                if(numberFeet > 9){
                    numberFeet = numberFeet.toString().substring(0, 1).toLong()
                }

                return "$numberMiles,$numberFeet ${MetricsCode.DISTANCE_MILE}"

            }else{
                return ""
            }
        }

        fun displayDurationFromDistanceFormatted(distance: Double) : String{
            val distanceInMeters = distance.roundToInt()
            var distanceInKm = (distanceInMeters.toFloat()/1000)
            Log.d("distanceInKm", distanceInKm.toString())
            var duration = (distanceInKm/4)*60
            Log.d("duration", duration.toString())
            var nbHours = 0
            var nbMinutes = 0
            if(duration > 60){
                var restMinutes = duration % 60
                nbHours = ((duration-restMinutes)/60).toInt()
                nbMinutes = restMinutes.toInt()
            }else{
                nbMinutes = duration.toInt()
            }
            if(nbHours < 1){
                return "$nbMinutes ${MetricsCode.TIME_MINUTE}"
            }else{
                return "$nbHours${MetricsCode.TIME_HOUR}$nbMinutes${MetricsCode.TIME_MINUTE}"
            }
        }

        fun displayDuration(durationInMinutes: Long?) : String{
            if (durationInMinutes == null) return ""
            if(durationInMinutes >= 60){
                var hours = (durationInMinutes/60).toInt()
                var minutes = durationInMinutes%60
                if(minutes == 0L)
                    return "$hours${MetricsCode.TIME_HOUR}"
                else
                    return "$hours${MetricsCode.TIME_HOUR}$minutes${MetricsCode.TIME_MINUTE}"
            }else{
                return "$durationInMinutes ${MetricsCode.TIME_MINUTE}"
            }
        }

        fun displayAscentFormatted(distance: Long?) : String{
            if(ApplicationRunner.appDistanceMetrics == MetricsCode.METRIC){
                return "$distance ${MetricsCode.DISTANCE_METERS}"
            }else if(ApplicationRunner.appDistanceMetrics == MetricsCode.IMPERIAL){
                var ascentRounded = (distance?.times(3.281))?.roundToInt()
                return "$ascentRounded ${MetricsCode.DISTANCE_FEET}"
            }else{
                return ""
            }
        }

        fun convertFromMinutesToMs(time: Long): Long{
            return time*60*1000
        }

        fun convertFromSecondsToMs(time: Long): Long{
            return time*1000
        }
    }
}