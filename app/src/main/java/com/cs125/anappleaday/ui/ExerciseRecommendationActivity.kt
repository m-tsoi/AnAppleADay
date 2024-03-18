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
import org.w3c.dom.Text
import java.util.Date
import kotlin.math.min

class ExerciseRecommendationActivity : AppCompatActivity() {
    private val exerciseTypes = listOf("Lifting", "Cardio", "Stretching")
    private lateinit var MET : TextView
    private lateinit var exerciseTypeEditText: TextView
    private lateinit var durationEditText: TextView
    private lateinit var displayTextView: TextView
    private lateinit var submitBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        submitBtn.setOnClickListener {
            val exerciseType = exerciseTypeEditText.text.toString()
            val duration = durationEditText.text.toString().toIntOrNull()

            if (exerciseTypes.contains(exerciseType) && duration != null && duration > 0) {
                val displayText = "Exercise Type: $exerciseType, Duration: $duration minutes"
                displayTextView.text = displayText
            } else {
                showToast("Please enter a valid exercise type (Lifting, Cardio, Yoga) and duration")
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this@ExerciseRecommendationActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun calculateMET(weight: Double, activityType: String): Double {
        // Assume a standard resting metabolic rate (RMR) of 1.0 kcal/kg/hour
        val rmr = 1.0

        // Define energy expenditure values for different activities (kcal/kg/hour)
        val energyExpenditureLifting = 3.0
        val energyExpenditureCardio = 7.0
        val energyExpenditureStretching = 2.0

        // Calculate MET based on the activity type
        val met = when (activityType.toLowerCase()) {
            "lifting" -> energyExpenditureLifting / rmr
            "cardio" -> energyExpenditureCardio / rmr
            "stretching" -> energyExpenditureStretching / rmr
            else -> 0.0 // Default to 0.0 for unknown activity types
        }

        return met
    }
}