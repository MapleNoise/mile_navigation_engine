package com.mile.mile_navigation_engine.interfaces

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Custom item click listener
 */

interface onItemClickListener : View.OnClickListener {

    fun onItemClick(holder: RecyclerView.ViewHolder)

}