package com.mile.mile_navigation_engine

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.R
import com.google.gson.Gson
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgressState
import com.mile.mile_navigation_engine.activities.navigation.NavigateToPOIActivity
import com.mile.mile_navigation_engine.activities.navigation.NavigationActivity
import com.mile.mile_navigation_engine.model.LanguageCode
import com.mile.mile_navigation_engine.model.POI
import com.mile.mile_navigation_engine.model.RouteDataClass
import com.mile.mile_navigation_engine.model.StartingPoint
import com.mile.mile_navigation_engine.utils.AppDataHolder
import io.flutter.app.FlutterApplication
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONArray
import org.json.JSONObject

class MileNavigationEngine : MethodChannel.MethodCallHandler, EventChannel.StreamHandler {

    var _activity: Activity
    var _context: Context

    var _distanceRemaining: Double? = null
    var _durationRemaining: Double? = null

    var PERMISSION_REQUEST_CODE: Int = 367


    constructor(context: Context, activity: Activity) {
        this._context = context
        this._activity = activity;
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

        var arguments = call.arguments as? Map<String, Any>

        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        }
        else if(call.method == "getDistanceRemaining") {
            result.success(_distanceRemaining);
        } else if(call.method == "getDurationRemaining") {
            result.success(_durationRemaining);
        } else if(call.method == "startNavigation") {

            val route = arguments?.get("currentRoute") as? String
            val gpsColor = arguments?.get("gpsColor") as? String
            val accessToken = arguments?.get("accessToken") as? String
            val mode = arguments?.get("mode") as? String

            if(route != null && gpsColor != null && accessToken != null && mode != null) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var haspermission = _activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    if(haspermission != PackageManager.PERMISSION_GRANTED) {
                        //_activity.onRequestPermissionsResult((a,b,c) => onRequestPermissionsResult)
                        _activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
                        startNavigation(route, gpsColor, accessToken, mode)
                    } else
                        startNavigation(route, gpsColor, accessToken, mode)
                }
                else
                    startNavigation(route, gpsColor, accessToken, mode)
            }
        }
        else {
            result.notImplemented()
        }
    }

    fun startNavigation(route: String, gpsColor: String, accessToken: String, mode: String) {
        Mapbox.getInstance(_context, accessToken)

        flutterRouteToKtRoute(route, gpsColor)

        var intent: Intent? = null;
        if(mode == NavigationMode.NAVIGATE_IN_ROUTE) {
            intent = Intent(_activity, NavigationActivity::class.java)
        } else if(mode == NavigationMode.NAVIGATE_TO_POI) {
            intent = Intent(_activity, NavigateToPOIActivity::class.java)
        }

        _activity.startActivity(intent)
    }

    override fun onListen(args: Any?, events: EventChannel.EventSink?) {
        AppDataHolder._eventSink = events;
    }

    override fun onCancel(args: Any?) {
        AppDataHolder._eventSink = null;
    }


    fun flutterRouteToKtRoute(routeJson: String?, gpsColor: String?) {

        //For parse automatically massive params
        var gson = Gson()
        var route = gson?.fromJson(routeJson, RouteDataClass::class.java)

        //For parse specific params
        val routeJSONObj = JSONObject(routeJson)
        if(routeJSONObj.has("methodLocomotion")){
            val methodLocomotion = routeJSONObj.get("methodLocomotion") as Integer
            route.methodLocomotion = RouteDataClass.Locomotion.getLocomotion(methodLocomotion.toLong())
        }

        if(routeJSONObj.has("arrayCoordinates")){
            var locations: ArrayList<Location> = ArrayList()
            val arrayCoordinates = routeJSONObj.get("arrayCoordinates") as JSONArray

            for (i in 0 until arrayCoordinates.length()) {
                val item = arrayCoordinates.getJSONObject(i)
                var location = Location("provider")
                location.latitude = (item.get("latitude")as Double?)!!
                location.longitude = (item.get("longitude") as Double?)!!
                locations.add(location)
            }
            route.arrayCoordinates = locations
            route.previewCoordinates = locations //Maybe redo
            AppDataHolder.currentStartingPoint = StartingPoint("", locations.get(0).latitude, locations.get(0).longitude, 0, 0) //Maybe redo
        }

        if(routeJSONObj.has("arrayDescription")){
            val arrayDescription = HashMap<LanguageCode, String>()
            val arrayDescriptionObj = routeJSONObj.get("arrayDescription") as JSONObject
            LanguageCode.values().forEach {
                if(arrayDescriptionObj.has(it.getLocaleLanguage().toUpperCase())) {
                    var description = arrayDescriptionObj.get(it.getLocaleLanguage().toUpperCase())
                    arrayDescription[it] = description as String
                }
            }
            route.arrayDescription = arrayDescription
        }

        if(routeJSONObj.has("arrayName")){
            val arrayName = HashMap<LanguageCode, String>()
            val arrayNameObj = routeJSONObj.get("arrayName") as JSONObject
            LanguageCode.values().forEach {
                if(arrayNameObj.has(it.getLocaleLanguage().toUpperCase())) {
                    var name = arrayNameObj.get(it.getLocaleLanguage().toUpperCase())
                    arrayName[it] = name as String
                }
            }
            route.arrayName = arrayName
        }

        if(routeJSONObj.has("arrayPois")){
            var pois: ArrayList<POI> = ArrayList()
            val arrayPois = routeJSONObj.get("arrayPois") as JSONArray

            for (i in 0 until arrayPois.length()) {
                val poiObj = arrayPois.getJSONObject(i)
                var poi = gson?.fromJson(poiObj.toString(), POI::class.java)

                if(poiObj.has("arrayCoordinates")){
                    var locations: ArrayList<Location> = ArrayList()
                    val arrayCoordinates = poiObj.get("arrayCoordinates") as JSONArray

                    for (i in 0 until arrayCoordinates.length()) {
                        val item = arrayCoordinates.getJSONObject(i)
                        var location = Location("provider")
                        location.latitude = (item.get("latitude")as Double?)!!
                        location.longitude = (item.get("longitude") as Double?)!!
                        locations.add(location)
                    }
                    poi.arrayCoordinates = locations
                }

                if(poiObj.has("type")){
                    val type = poiObj.get("type") as Integer
                    poi.type = POI.Type.get(type.toLong())
                }

                if(poiObj.has("arrayDescription")){
                    val arrayDescription = HashMap<LanguageCode, String>()
                    val arrayDescriptionObj = poiObj.get("arrayDescription") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayDescriptionObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var description = arrayDescriptionObj.get(it.getLocaleLanguage().toUpperCase())
                            arrayDescription[it] = description as String
                        }
                    }
                    poi.arrayDescription = arrayDescription
                }

                if(poiObj.has("arrayName")){
                    val arrayName = HashMap<LanguageCode, String>()
                    val arrayNameObj = poiObj.get("arrayName") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayNameObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var name = arrayNameObj.get(it.getLocaleLanguage().toUpperCase())
                            arrayName[it] = name as String
                        }
                    }
                    poi.arrayName = arrayName
                }

                if(poiObj.has("arrayMP3")){
                    val array = HashMap<LanguageCode, String>()
                    val arrayObj = poiObj.get("arrayMP3") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var description = arrayObj.get(it.getLocaleLanguage().toUpperCase())
                            array[it] = description as String
                        }
                    }
                    poi.arrayMP3 = array
                }

                if(poiObj.has("arrayTextMP3")){
                    val array = HashMap<LanguageCode, String>()
                    val arrayObj = poiObj.get("arrayTextMP3") as JSONObject
                    LanguageCode.values().forEach {
                        if(arrayObj.has(it.getLocaleLanguage().toUpperCase())) {
                            var description = arrayObj.get(it.getLocaleLanguage().toUpperCase())
                            array[it] = description as String
                        }
                    }
                    poi.arrayTextMP3 = array
                }

                pois.add(poi)
                route.arrayPois = pois
            }
        } else {
            route.arrayPois = ArrayList()
        }

        AppDataHolder.currentRoute = route
        AppDataHolder.gpsColor = gpsColor
        //AppDataHolder.applicationInstance = this.application as FlutterApplication
    }
}