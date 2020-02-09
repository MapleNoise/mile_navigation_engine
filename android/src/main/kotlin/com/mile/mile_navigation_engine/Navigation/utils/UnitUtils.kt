package com.mile.mile_navigation_engine.utils

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt


/**
Created by Corentin Houdayer on 2019-09-11
Dev profile @ https://github.com/houdayec
 **/

class UnitUtils {

    companion object{

        /**
         * Converts DP into pixels.
         *
         * @param dp The value in DP to be converted into pixels.
         *
         * @return The converted value in pixels.
         */
        fun convertDpToPixels(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().displayMetrics
            ).roundToInt()
        }

        /**
         * Converts pixels into DP.
         *
         * @param pixels The value in pixels to be converted into DP.
         *
         * @return The converted value in DP.
         */
        fun convertPixelsToDp(pixels: Float): Int {
            return (pixels / Resources.getSystem().displayMetrics.density).roundToInt()
        }

        /**
         * Converts SP into pixels.
         *
         * @param sp The value in SP to be converted into pixels.
         *
         * @return The converted value in pixels.
         */
        fun convertSpToPixels(sp: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp, Resources.getSystem().displayMetrics
            ).roundToInt().toFloat()
        }

        /**
         * Converts pixels into SP.
         *
         * @param pixels The value in pixels to be converted into SP.
         *
         * @return The converted value in SP.
         */
        fun convertPixelsToSp(pixels: Float): Float {
            return (pixels / Resources.getSystem().displayMetrics.scaledDensity).roundToInt().toFloat()
        }

        /**
         * Converts DP into SP.
         *
         * @param dp The value in DP to be converted into SP.
         *
         * @return The converted value in SP.
         */
        fun convertDpToSp(dp: Float): Float {
            return (convertDpToPixels(dp) / convertSpToPixels(dp)).roundToInt().toFloat()
        }
    }
}