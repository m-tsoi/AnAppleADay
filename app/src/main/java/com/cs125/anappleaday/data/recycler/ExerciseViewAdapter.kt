package com.cs125.anappleaday.data.recycler
import android.view.LayoutInflater
import com.cs125.anappleaday.R

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseViewAdapter(private val dataSet: MutableList<String>) :
    RecyclerView.Adapter<ExerciseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_view, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.textName.text = dataSet[position]
        holder.buttonDelete.setOnClickListener {
            dataSet.removeAt(position) // may have bug if rapidly clicked but uh
            notifyItemRemoved(position)
        }
    }
    override fun getItemCount() = dataSet.size
}
class ExerciseViewHolder(dietView: View) : RecyclerView.ViewHolder(dietView) {
    var textName : TextView =  dietView.findViewById(R.id.textName)
    var buttonDelete : Button = dietView.findViewById(R.id.buttonDelete)
}