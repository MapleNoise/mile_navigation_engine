package com.mile.mile_navigation_engine.customviews.components

/**
Created by Corentin Houdayer on 2019-09-13
Dev profile @ https://github.com/houdayec
 **/

interface OnRecyclerViewScrolledListener {
    fun onScrollStateChanged(newState: Int)
    fun onScrolled(coords: Pair<Int,Int>)
}