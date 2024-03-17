package com.cs125.anappleaday.data.recycler
import android.view.LayoutInflater
import com.cs125.anappleaday.R

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DietRecAdapter(private val dataSet: MutableList<String>) :
    RecyclerView.Adapter<DietRecHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietRecHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diet_rec, parent, false)
        return DietRecHolder(view)
    }

    override fun onBindViewHolder(holder: DietRecHolder, position: Int) {
        holder.textName.text = dataSet[position]
    }
    override fun getItemCount() = dataSet.size
}
class DietRecHolder(dietRec: View) : RecyclerView.ViewHolder(dietRec) {
    var textName : TextView =  dietRec.findViewById(R.id.textName)
}