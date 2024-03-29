package com.cs125.anappleaday.data.recycler
import android.view.LayoutInflater
import com.cs125.anappleaday.R

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.data.enumTypes.NutritionData

class DietViewAdapter(private val dataSet: MutableList<NutritionData>, private val onDeleteClickListener: (NutritionData) -> Unit) :
    RecyclerView.Adapter<DietViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diet_view, parent, false)
        return DietViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        holder.textName.text = dataSet[position].name
        holder.buttonDelete.setOnClickListener {
            val deletedNutritionData = dataSet[position]
            onDeleteClickListener.invoke(deletedNutritionData) // outside function to delete from db
            dataSet.removeAt(position) // may have bug if rapidly clicked but uh
            notifyItemRemoved(position)
        }
    }
    override fun getItemCount() = dataSet.size
}
class DietViewHolder(dietView: View) : RecyclerView.ViewHolder(dietView) {
    var textName : TextView =  dietView.findViewById(R.id.textName)
    var buttonDelete : Button = dietView.findViewById(R.id.buttonDelete)
}