package com.mile.mile_navigation_engine.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.services.android.navigation.v5.navigation.*
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.R
import timber.log.Timber

/**
Created by Corentin Houdayer on 2019-09-20
Dev profile @ https://github.com/houdayec
 **/

class OfflineRouteManager {

    companion object{

        var offlineRouter = MapboxOfflineRouter("mythalassa")

        fun fetchTileVersions(ctx: Context){
            offlineRouter.fetchAvailableTileVersions(Mapbox.getAccessToken(), object :
                OnTileVersionsFoundCallback {
                override fun onVersionsFound(availableVersions: List<String>) {
                    // Choose an available version for downloading tiles
                    downloadTiles(availableVersions.last())
                }

                override fun onError(error: OfflineError) {
                    Toast.makeText(ctx, "Unable to get versions", Toast.LENGTH_LONG).show()
                }
            })
        }

        fun downloadTiles(version: String){
            val builder = OfflineTiles.builder()
                .accessToken(Mapbox.getAccessToken())
                .version(version)
                .boundingBox(BoundingBox.fromLngLats(45.807203, 4.761539, 45.807203, 4.761539))

            offlineRouter.downloadTiles(builder.build(), object : RouteTileDownloadListener {

                override fun onError(error: OfflineError) {
                    // Will trigger if an error occurs during the download
                    Timber.e("error while downloading tiles : ${error.message}")
                }

                override fun onProgressUpdate(percent: Int) {
                    // Will update with percent progress of the download
                    Timber.d("downloading tiles... $percent")
                }

                override fun onCompletion() {
                    offlineRouter.configure(version, object : OnOfflineTilesConfiguredCallback {

                        override fun onConfigured(numberOfTiles: Int) {
                            Log.d(OfflineRouteManager.javaClass.simpleName, ("Offline tiles configured: $numberOfTiles"))
                            // Fetch offline route
                                val onlineRouteBuilder = NavigationRoute.builder(ApplicationRunner.instance)
                                .origin(Point.fromLngLat(45.790512, 4.790218))
                                .destination(Point.fromLngLat(45.794170, 4.791989))
                                .accessToken(ApplicationRunner.instance.getString(R.string.access_token))
                            val offlineRoute = OfflineRoute.builder(onlineRouteBuilder).build()
                            offlineRouter.findRoute(offlineRoute, object : OnOfflineRouteFoundCallback {
                                override fun onRouteFound(route: DirectionsRoute) {
                                // Start navigation with route
                                }

                                override fun onError(error: OfflineError) {
                                // Handle route error
                                }
                            })
                        }

                        override fun onConfigurationError(error: OfflineError) {
                            Log.d(OfflineRouteManager.javaClass.simpleName, ("Offline tiles configuration error: {${error.message}}"))
                        }
                    })
                }
            })
        }
    }
}