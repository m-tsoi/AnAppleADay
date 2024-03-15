package com.cs125.anappleaday.ui

import android.R
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.cs125.anappleaday.R


class YourActivity : AppCompatActivity() {

    private lateinit var autoCompleteTextViewExercise: AutoCompleteTextView
    private lateinit var exerciseAdapter: ArrayAdapter<String>


    class CountriesActivity : Activity() {
        override fun onCreate(icicle: Bundle?) {
            super.onCreate(icicle)
            setContentView(R.layout.countries)
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES
            )
            val textView = findViewById<View>(R.id.countries_list) as AutoCompleteTextView
            textView.setAdapter(adapter)
        }

        companion object {
            private val COUNTRIES = arrayOf(
                "Belgium", "France", "Italy", "Germany", "Spain"
            )
        }
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_exercise)
//
//        // Initialize AutoCompleteTextView
//        autoCompleteTextViewExercise = findViewById(R.id.autoCompleteTextViewExercise)
//
//        // Initialize adapter for AutoCompleteTextView
//        exerciseAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)
//        autoCompleteTextViewExercise.setAdapter(exerciseAdapter)
//
//        // Make API call to fetch exercise suggestions
//        fetchExerciseSuggestions()
//    }
//
//    private fun fetchExerciseSuggestions() {
//        val apiServices = ApiMain.getApiServices()
//        val call = apiServices.getExerciseSuggestions()
//
//        call.enqueue(object : Callback<List<String>> {
//            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
//                if (response.isSuccessful) {
//                    val suggestions = response.body()
//                    suggestions?.let {
//                        exerciseAdapter.addAll(it)
//                        exerciseAdapter.notifyDataSetChanged()
//                    }
//                } else {
//                    // Handle unsuccessful response
//                }
//            }
//
//            override fun onFailure(call: Call<List<String>>, t: Throwable) {
//                // Handle failure
//            }
//        })
//    }
}