package com.mile.mile_navigation_engine.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE

/**
Created by Corentin Houdayer on 2019-09-05
Dev profile @ https://github.com/houdayec
 **/

class PermissionManager {

    companion object{

        var PERMISION_REQUEST_LOCATION = 1000

        fun checkIfLocationIsGranted(ctx: Activity) : Boolean{

            if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(ctx, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION))
                return false
            }else
                return true
        }

        fun requestPermissions(ctx: Activity, permissions: Array<String>){
            ActivityCompat.requestPermissions(ctx, permissions, PERMISION_REQUEST_LOCATION)
        }

        fun isLocationActivated(ctx: Activity) : Boolean{
            val service = ctx.getSystemService(LOCATION_SERVICE) as LocationManager?
            val enabled = service!!
                .isProviderEnabled(LocationManager.GPS_PROVIDER)

            // Check if enabled and if not send user to the GSP settings
            // Better solution would be to display a dialog and suggesting to
            // go to the settings
            if (!enabled) {
                //val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                //ctx.startActivity(intent)
                return false
            }else{
                return true
            }
        }
    }
}