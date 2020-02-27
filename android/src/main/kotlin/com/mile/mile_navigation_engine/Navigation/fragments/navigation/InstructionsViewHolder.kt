package com.mile.miramas.fragments.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.R
import com.mile.mile_navigation_engine.model.POI

class InstructionsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_treasure_hunt_instruction, parent, false)) {

    private var index_instruction : TextView
    private var text_instruction : TextView

    init {
        index_instruction = itemView.findViewById(R.id.index_instruction)
        text_instruction = itemView.findViewById(R.id.text_instruction)
    }

    fun bind(poi: POI, passed: Boolean) {
        this.index_instruction.text = String.format(ApplicationRunner.instance.getString(R.string.title_instruction_riddle_index, poi.riddle.index))
        this.text_instruction.text = poi.riddle.instruction
        if(passed){
            this.index_instruction.setTextColor(ApplicationRunner.instance.resources.getColor(R.color.grey))
            this.text_instruction.setTextColor(ApplicationRunner.instance.resources.getColor(R.color.grey))
        }
    }

}