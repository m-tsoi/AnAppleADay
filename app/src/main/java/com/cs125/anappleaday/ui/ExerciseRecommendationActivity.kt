package com.cs125.anappleaday.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import android.app.TimePickerDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import nl.joery.timerangepicker.TimeRangePicker
import java.util.Date
import kotlin.math.min
class ExerciseRecommendationActivity : AppCompatActivity() {

    private val exercisesList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private var userWeight: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_recommendation)

        // Initialize the adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exercisesList)

        // Set the adapter for exerciseListView
        exerciseListView.adapter = adapter

        // Click listener for addExerciseButton
        addExerciseButton.setOnClickListener {
            val newExercise = exerciseEditText.text.toString().trim()
            val durationText = exerciseDurationEditText.text.toString().trim()
            if (newExercise.isNotEmpty() && durationText.isNotEmpty()) {
                val duration = durationText.toInt()
                val weightLoss = calculateWeightLoss(newExercise, duration)
                val message = "You burned $weightLoss kg by doing $newExercise for $duration minutes"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                exercisesList.add("$newExercise - $duration min")
                adapter.notifyDataSetChanged()
                exerciseEditText.text.clear()
                exerciseDurationEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter an exercise and duration", Toast.LENGTH_SHORT).show()
            }
        }

        // Check if it's the first time the user opens the app
        val sharedPref = getPreferences(MODE_PRIVATE)
        if (!sharedPref.contains("userWeight")) {
            // If it's the first time, prompt the user to input their weight
            inputUserWeight()
        } else {
            // If weight is already stored, retrieve it
            userWeight = sharedPref.getFloat("userWeight", 0.0f).toDouble()
        }
    }

    private fun inputUserWeight() {
        // Prompt the user to input their weight
        // You can customize this dialog according to your UI design
        // For simplicity, I'll just use a Toast as an example
        Toast.makeText(this, "Please input your weight", Toast.LENGTH_SHORT).show()

        // For demonstration purposes, let's assume the user inputs the weight through another interface
        // and stores it in the shared preferences
        val editor = getPreferences(MODE_PRIVATE).edit()
        editor.putFloat("userWeight", userWeight.toFloat())
        editor.apply()
    }

    private fun calculateWeightLoss(exerciseType: String, duration: Int): Double {
        val intensityFactor = when (exerciseType.toLowerCase()) {
            "running" -> 0.1
            "weight lifting" -> 0.05
            "yoga" -> 0.03
            "swimming" -> 0.08
            else -> 0.0
        }
        return intensityFactor * duration
    }
}
