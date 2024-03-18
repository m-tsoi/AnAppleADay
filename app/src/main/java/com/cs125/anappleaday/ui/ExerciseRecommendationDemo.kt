package com.cs125.anappleaday.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.user.Profile

class ExerciseRecommendationDemo : AppCompatActivity() {
    private lateinit var exerciseSpinner: Spinner
    private lateinit var durationEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_demo)

        exerciseSpinner = findViewById(R.id.exerciseSpinner)
        durationEditText = findViewById(R.id.durationEditText)
        submitButton = findViewById(R.id.submitButton)
        scoreTextView = findViewById(R.id.scoreTextView)

        val exerciseTypes = arrayOf("Lifting", "Cardio", "Stretching")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exerciseSpinner.adapter = adapter

        submitButton.setOnClickListener {
            val exerciseType = exerciseSpinner.selectedItem.toString()
            val duration = durationEditText.text.toString().toIntOrNull() ?: 0

            var score = 0
            when (exerciseType) {
                "Lifting" -> {
                    if (duration == 46) {
                        score = 54
                    }
                }
                // Add cases for other exercise types if needed
            }

            scoreTextView.text = "Score: $score"
        }
    }
}
