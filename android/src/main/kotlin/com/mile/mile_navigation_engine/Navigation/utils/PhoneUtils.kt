package com.mile.mile_navigation_engine.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.R

class PhoneUtils{
    companion object{

        fun triggerVibration(durationInMs: Long){
            /*var v = AppDataHolder.applicationInstance?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(durationInMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                v.vibrate(durationInMs)
            }*/
        }

        fun checkIfLocationIsActivated(passedContext: Context): Boolean{
            var lm = AppDataHolder.applicationInstance?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var gps_enabled = false
            var network_enabled = false

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch(ex: Exception) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            } catch(ex: Exception) {}

            if(!gps_enabled && !network_enabled) {
                // notify user
                AlertDialog.Builder(passedContext)
                    .setTitle(R.string.text_gps_network_not_enabled)
                    .setMessage(R.string.message_gps_network_not_enabled)
                    .setPositiveButton(R.string.action_open_location_settings, object: DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            passedContext.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }

                    })
                    .setNegativeButton(R.string.label_cancel,null)
                    .show()
                return false
            }else{
                return true
            }
        }
    }
}