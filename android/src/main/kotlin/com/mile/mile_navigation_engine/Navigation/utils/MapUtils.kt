package com.mile.mile_navigation_engine.utils

import android.content.Context
import android.location.Location
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.map.NavigationMapboxMap
import com.mapbox.turf.TurfMeasurement
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import com.mile.mile_navigation_engine.Navigation.utils.CoordinateUtils
import com.mile.mile_navigation_engine.model.POI


/**
Created by Corentin Houdayer on 2019-08-28
Dev profile @ https://github.com/houdayec
 **/

class MapUtils {

    companion object{

        fun convertCoordinatesToPoints(arrayCoordinates : List<Location>) : ArrayList<Point>{
            var arrayLatLng = ArrayList<Point>()
            arrayCoordinates.forEach {
                arrayLatLng.add(Point.fromLngLat(it.longitude, it.latitude))
            }
            return arrayLatLng
        }

        fun convertPointsToLatLng(arrayCoordinates : List<Point>) : ArrayList<LatLng>{
            var arrayLatLng = ArrayList<LatLng>()
            arrayCoordinates.forEach {
                arrayLatLng.add(LatLng(it.latitude(), it.longitude()))
            }
            return arrayLatLng
        }

        fun convertLocationToPoint(location: Location) : Point{
            return Point.fromLngLat(location.longitude, location.latitude)
        }

        fun convertPointToLocation(point: Point) : Location{
            var location = Location("provider")
            location.longitude = point.longitude()
            location.latitude = point.latitude()
            return location
        }


        fun computeRouteCoordinatesBasedOnClosestPoint(userLocation : Location, arrayCoordinates: List<Location>) : List<Location>{
            var closestPoint = getClosestPointFromUser(userLocation, convertCoordinatesToPoints(arrayCoordinates))
            var positionToCut = arrayCoordinates.indexOf(convertPointToLocation(closestPoint))
            arrayCoordinates.forEachIndexed { index, location ->
                if(location.latitude == closestPoint.latitude() && location.longitude == closestPoint.longitude()){
                    positionToCut = index
                }
            }
            if(positionToCut != 0){
                var firstPart = arrayCoordinates.subList(0, positionToCut-1)
                var secondPart = arrayCoordinates.subList(positionToCut, arrayCoordinates.size)
                val finalList = secondPart.toMutableList()
                finalList += firstPart
                return finalList
            }else{
                return arrayCoordinates
            }
        }

        fun getClosestPointFromUser(userLocation: Location, routeCoordinates: List<Point>) : Point{
            var minDistance = 20000.0
            var userLocationPoint = MapUtils.convertLocationToPoint(userLocation)
            var closestPoint = Point.fromLngLat(userLocation.longitude, userLocation.latitude)

            routeCoordinates.forEach { point ->
                var distance = TurfMeasurement.distance(userLocationPoint, point)
                if(distance < minDistance){
                    closestPoint = point
                    minDistance = distance
                }
            }

            return closestPoint
        }

        fun getRouteDistanceFromUser(userLocation: Location, routeCoordinates: List<Point>) : Double{
            var minDistance = 20000.0
            var userLocationPoint = MapUtils.convertLocationToPoint(userLocation)
            var closestPoint = Point.fromLngLat(userLocation.longitude, userLocation.latitude)

            routeCoordinates.forEach { point ->
                var distance = TurfMeasurement.distance(userLocationPoint, point)
                if(distance < minDistance){
                    closestPoint = point
                    minDistance = distance
                }
            }

            return minDistance
        }

        fun getDistanceFromPOI(userLocation: Location, poi : POI) : Double{
            return TurfMeasurement.distance(convertLocationToPoint(userLocation), convertLocationToPoint(poi.mainCoordinates))
        }

        fun getDistanceFromLocation(originLocation: Location, userLocation: Location) : Double{
            return TurfMeasurement.distance(convertLocationToPoint(userLocation), convertLocationToPoint(originLocation))
        }

        fun getDistancesFromPOI(userLocation: Location, poi : POI) : List<Double>{
            var listDistances = ArrayList<Double>()
            poi.arrayCoordinates.forEach { location ->
                listDistances.add(TurfMeasurement.distance(convertLocationToPoint(userLocation), convertLocationToPoint(location)))
            }
            return listDistances
        }

        fun getFacingDirection(previousLocation: Location, actualLocation: Location, poi: POI): String {

            val projection = CoordinateUtils.projectOnNewReference(previousLocation, actualLocation, poi.mainCoordinates)
            val distance = poi.mainCoordinates.distanceTo(projection.getProjectionCoordinate())

            return if (distance > 10) {
                if (projection.getParentProjectionCoordinateInNewReference().getLongitude() > 0) {
                    LanguageResource.languagesResources.right
                } else {
                    LanguageResource.languagesResources.left
                }
            } else {
                //POI is in a 10m range from the user, so it's rather In Front than Left/Right
                LanguageResource.languagesResources.front
            }
        }

        fun loadPOIIndexed(ctx: Context, v: View): Bitmap {
            if (v.layoutParams == null) {
                //v.measure(UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.width_bubble_poi)), UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.height_bubble_poi)))
                v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                //val b = Bitmap.createBitmap(UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.width_bubble_poi)), UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.height_bubble_poi)), Bitmap.Config.ARGB_8888)
                val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
                val c = Canvas(b)
                //v.layout(0, 0, UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.width_bubble_poi)), UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.height_bubble_poi)))
                v.layout(0, 0, v.measuredWidth, v.measuredHeight)
                v.draw(c)
                return b
            }else{
                val b = Bitmap.createBitmap(
                        v.layoutParams.width,
                        v.layoutParams.height,
                        Bitmap.Config.ARGB_8888
                )
                val c = Canvas(b)
                v.layout(v.left, v.top, v.right, v.bottom)
                v.draw(c)
                return b
            }
        }

        fun loadPOIAnnotation(ctx: Context, v: View): Bitmap {
            if (v.layoutParams == null) {
                //v.measure(UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.width_bubble_poi)), UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.height_bubble_poi)))
                v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                //val b = Bitmap.createBitmap(UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.width_bubble_poi)), UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.height_bubble_poi)), Bitmap.Config.ARGB_8888)
                val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
                val c = Canvas(b)
                //v.layout(0, 0, UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.width_bubble_poi)), UnitUtils.convertDpToPixels(ctx.resources.getDimension(R.dimen.height_bubble_poi)))
                v.layout(0, 0, v.measuredWidth, v.measuredHeight)
                v.draw(c)
                return b
            }else{
                val b = Bitmap.createBitmap(
                        v.layoutParams.width,
                        v.layoutParams.height,
                        Bitmap.Config.ARGB_8888
                )
                val c = Canvas(b)
                v.layout(v.left, v.top, v.right, v.bottom)
                v.draw(c)
                return b
            }
        }

        fun shouldDisplayPOI(poi: POI): Boolean{
            return !(poi.type == POI.Type.DIRECTIONNAL_COUNTERCLOCKWISE || poi.type == POI.Type.DIRECTIONNAL_CLOCKWISE || poi.type == POI.Type.NO_ANNOTATION)
        }

        fun shouldDisplayPOICateg(poiType: POI.Type): Boolean{
            return !(poiType == POI.Type.DIRECTIONNAL_COUNTERCLOCKWISE || poiType == POI.Type.DIRECTIONNAL_CLOCKWISE || poiType == POI.Type.NO_ANNOTATION)
        }

    }

}