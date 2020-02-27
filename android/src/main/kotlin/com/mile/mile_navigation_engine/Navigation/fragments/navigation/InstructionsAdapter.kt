package com.mile.miramas.fragments.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.mile.mile_navigation_engine.model.POI

class InstructionsAdapter(var listPOI: ArrayList<POI>, var currentRiddleIndex: Long, val context: Context) : RecyclerView.Adapter<InstructionsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return InstructionsViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return listPOI.size
    }

    override fun onBindViewHolder(holder: InstructionsViewHolder, position: Int) {
        val item = listPOI[position]
        var hasItemBeenPassed = false
        if(position < currentRiddleIndex-1) hasItemBeenPassed = true
        holder.bind(item, hasItemBeenPassed)

        // TODO click if needed
    }

    interface OnItemClickListener{
        fun onHistoryClicked(historyClicked : POI, position: Int)
    }

}