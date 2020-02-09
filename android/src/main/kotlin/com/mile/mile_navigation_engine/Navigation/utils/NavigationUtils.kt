package com.mile.mile_navigation_engine.utils

import com.yashovardhan99.timeit.Stopwatch

/**
Created by Corentin Houdayer on 2019-10-29
Dev profile @ https://github.com/houdayec
 **/

class NavigationUtils {

    companion object{

        var currentStopWatchTimer : Stopwatch? = null

        fun startTimer(){
            currentStopWatchTimer = Stopwatch()
            currentStopWatchTimer?.start()
        }

        fun stopTimer(){
            currentStopWatchTimer?.stop()
        }
    }

}