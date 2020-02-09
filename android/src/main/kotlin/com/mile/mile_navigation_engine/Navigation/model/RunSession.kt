package com.mile.mile_navigation_engine.model




class RunSession(routeStarted: RouteDataClass, startingTimestamp: Long) {

    var route = routeStarted
    var startingTimestamp = startingTimestamp
    var totalDistance: Double = 0.0
    var distance: Double = 0.0
    var duration: Long = 0L
    var arraySpeed: ArrayList<Double> = ArrayList()

    fun getAverageSpeed(): Double{
        return arraySpeed.average()
    }

    fun saveRunInDatabase(){
    }

    companion object{

        var currentSession: RunSession? = null

        fun initSession(routeStarted: RouteDataClass, startingTimestamp: Long): RunSession{
            currentSession = RunSession(routeStarted, startingTimestamp)
            return currentSession!!
        }
    }

}