package com.cs125.anappleaday.data.recycler
import android.view.LayoutInflater
import com.cs125.anappleaday.R

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.data.enumTypes.ExerciseData


class ExerciseResultAdapter(private var dataSet: MutableList<ExerciseData>, private val onAddClickListener: OnAddClickListener) :
    RecyclerView.Adapter<ExerciseResultHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseResultHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diet_result, parent, false)
        return ExerciseResultHolder(view)
    }

    interface OnAddClickListener {
        fun onAddClick(position: Int)
    }
    override fun onBindViewHolder(holder: ExerciseResultHolder, position: Int) {
        holder.textName.text = dataSet[position].name
        holder.buttonAdd.setOnClickListener {
            onAddClickListener.onAddClick(position)
        }
    }
    override fun getItemCount() = dataSet.size

    fun updateDataSet(newDataSet: MutableList<ExerciseData>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }


}
class ExerciseResultHolder(dietResult: View) : RecyclerView.ViewHolder(dietResult) {
    var textName : TextView =  dietResult.findViewById(R.id.textName)
    var buttonAdd : Button = dietResult.findViewById(R.id.buttonAdd)
}