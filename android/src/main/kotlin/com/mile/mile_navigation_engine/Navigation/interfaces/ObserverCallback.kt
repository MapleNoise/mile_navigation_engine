package com.mile.mile_navigation_engine.interfaces

import java.lang.Exception

/**
Created by Corentin Houdayer on 2019-09-06
Dev profile @ https://github.com/houdayec
 **/

interface ObserverCallback<T>{
    fun onSuccess(data: T)
    fun onFailed(exception: Exception)
}