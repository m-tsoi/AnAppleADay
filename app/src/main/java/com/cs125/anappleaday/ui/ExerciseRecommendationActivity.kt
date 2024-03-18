package com.cs125.anappleaday.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.TextView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.user.Profile

class ExerciseRecommendationActivity : AppCompatActivity() {
    private val exerciseTypes = listOf("Lifting", "Cardio", "Stretching")
    private lateinit var exerciseTypeEditText: TextView
    private lateinit var durationEditText: TextView
    private lateinit var submitBtn: Button
    private lateinit var addedExerciseTextView: TextView
    private lateinit var userProfile: Profile // Assuming userProfile is obtained from somewhere

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        exerciseTypeEditText = findViewById(R.id.exerciseTypeEditText)
        durationEditText = findViewById(R.id.durationEditText)
        submitBtn = findViewById(R.id.submitBtn)
        addedExerciseTextView = findViewById(R.id.addedExerciseTextView)

        submitBtn.setOnClickListener {
            val exerciseType = exerciseTypeEditText.text.toString()
            val duration = durationEditText.text.toString().toIntOrNull()

            if (exerciseTypes.contains(exerciseType) && duration != null && duration > 0) {
                val displayText = "Exercise Type: $exerciseType, Duration: $duration minutes"

                // Calculate MET
                val weight = userProfile.weight.toDouble() // Get user's weight
                val MET = calculateMET(weight, exerciseType)

                // Calculate calories burned
                val caloriesBurned = calculateCaloriesBurned(MET, weight, duration.toString())

                // Display
                val resultText = "$displayText\nCalories Burned: $caloriesBurned kcal"
                addedExerciseTextView.text = resultText
            } else {
                showToast("Please enter a valid exercise type (Lifting, Cardio, Yoga) and duration")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ExerciseRecommendationActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun calculateMET(weight: Double, activityType: String): Double {
        // Assume: standard resting metabolic rate of 1.0 kcal/kg/hour
        // May change
        val rmr = 1.0

        val energyExpenditureLifting = 3.0
        val energyExpenditureCardio = 7.0
        val energyExpenditureStretching = 2.0

        // MET by activity
        val met = when (activityType.toLowerCase()) {
            "lifting" -> energyExpenditureLifting / (weight * rmr)
            "cardio" -> energyExpenditureCardio / (weight * rmr)
            "stretching" -> energyExpenditureStretching / (weight * rmr)
            else -> 0.0
        }
        return met
    }

    private fun calculateCaloriesBurned(weight: Double, duration: Double, activityType: String): Double {
        val met = calculateMET(weight, activityType)
        val caloriesPerMinute = met * weight * 4 / 200
        return caloriesPerMinute * duration
    }

}