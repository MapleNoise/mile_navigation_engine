package com.mile.mile_navigation_engine.interfaces

/**
Created by Corentin Houdayer on 2019-09-05
Dev profile @ https://github.com/houdayec
 **/

interface MP3Listener {
    fun initMP3Duration(duration: Long)
    fun onListeningProgress(initialDuration: Long, currentProgress: Int)
    fun onListeningFinished()
}