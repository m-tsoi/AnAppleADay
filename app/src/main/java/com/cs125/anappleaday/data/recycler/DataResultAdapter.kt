package com.cs125.anappleaday.data.recycler
import android.view.LayoutInflater
import com.cs125.anappleaday.R

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.data.enumTypes.NutritionData


class DietResultAdapter(private var dataSet: MutableList<NutritionData>, private val onAddClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<DietResultHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietResultHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diet_result, parent, false)
        return DietResultHolder(view)
    }
    override fun onBindViewHolder(holder: DietResultHolder, position: Int) {
        holder.textName.text = dataSet[position].name
        holder.buttonAdd.setOnClickListener {
            onAddClickListener.invoke(position)
        }
    }
    override fun getItemCount() = dataSet.size
}
class DietResultHolder(dietResult: View) : RecyclerView.ViewHolder(dietResult) {
    var textName : TextView =  dietResult.findViewById(R.id.textName)
    var buttonAdd : Button = dietResult.findViewById(R.id.buttonAdd)
}