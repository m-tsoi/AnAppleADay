package com.cs125.anappleaday.ui

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.live.SleepData
import com.cs125.anappleaday.data.record.models.live.SleepSession
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import nl.joery.timerangepicker.TimeRangePicker
import java.util.Date
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * GET List Resources
 */
val call = apiServices.getRecommendedExercises(param1, param2, param3) // params are query params in url
call.enqueue(object : Callback<RecommendedExercises> {
    override fun onResponse(call: Call<RecommendedExercises>, response: Response<RecommendedExercises>) {
        val exercises = response.body()
        // do something here
    }

    override fun onFailure(call: Call<RecommendedExercises>, t: Throwable) {
        // do something here
        call.cancel()
    }
})
class ExerciseActivity : AppCompatActivity() {

    private lateinit var MET : TextView // Metabolic Equivalent of Task
    private lateinit var calories_burned : TextView // Calories burned
    private lateinit var calories_total : TextView // Calorie total from yesterday (figure out var)
    private lateinit var exercise_duration: TextView
    private lateinit var weight: TextView //Placeholder for how I would obtain weight from personicle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        MET = findViewById(R.id.MET)
        weight = findViewById(R.id.weight)
        exercise_duration = findViewById(R.id.exercise_duration)
        calories_burned = findViewById(R.id.calories_burned)

        // Assuming you have obtained MET, weight, and exercise duration
        val metValue: Double = MET.text.toString().toDoubleOrNull() ?: 0.0
        val personWeight: Double = weight.text.toString().toDoubleOrNull() ?: 0.0
        val durationHours: Double = exercise_duration.text.toString().toDoubleOrNull() ?: 0.0

        // Calculate calories burned
        val caloriesBurnedValue: Double = caloriesBurnedCalculation(metValue, personWeight, durationHours)

        // Display the calculated calories burned
        calories_burned.text = "Calories burned: ${caloriesBurnedValue.roundToInt()}"
    }

    private fun caloriesBurnedCalculation(met: Double, weight: Double, durationHours: Double): Double {
        return met * weight * durationHours
    }
}
