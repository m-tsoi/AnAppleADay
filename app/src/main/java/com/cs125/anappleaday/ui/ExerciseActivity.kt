package com.cs125.anappleaday.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.cs125.anappleaday.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.cs125.anappleaday.data.ApiMain
import com.cs125.anappleaday.data.RecommendedExercises
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt
class ExerciseActivity : AppCompatActivity() {

    // Exists to do later calculations for health

    private lateinit var MET: TextView // Metabolic Equivalent of Task
    private lateinit var calories_burned: TextView
    private lateinit var exercise_duration: TextView

    //Placeholders until retrieving stored data
    private lateinit var calories_total: TextView // calories_burned +/- calories_total + diet
    private lateinit var weight: TextView //Placeholder for how I would obtain weight from personicle

    private lateinit var recyclerRecommendations : RecyclerView //Helps recycle data already loaded, exercise data

    private lateinit var submit_calories : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        MET = findViewById(R.id.MET)
        calories_burned = findViewById(R.id.calories_burned)
        weight = findViewById(R.id.weight)
        exercise_duration = findViewById(R.id.exercise_duration)

        // MET, weight, and exercise duration
        // toDoubleOrNull is like ternary statement in JavaScript, return double if double, return null if nondouble
        val metValue: Double = MET.text.toString().toDoubleOrNull() ?: 0.0
        val personWeight: Double = weight.text.toString().toDoubleOrNull() ?: 0.0
        val durationHours: Double = exercise_duration.text.toString().toDoubleOrNull() ?: 0.0

        // Calculate calories burned using private function
        val caloriesBurnedValue: Double = caloriesBurnedCalculation(metValue, personWeight, durationHours)
        calories_burned.text = "Calories burned: ${caloriesBurnedValue.roundToInt()}"

        // Make API call
        val apiServices = ApiMain.getAPIServices()
        val call = apiServices.getRecommendedExercises(param1, param2, param3) //Edit params
        call.enqueue(object : Callback<RecommendedExercises> {
            override fun onResponse(call: Call<RecommendedExercises>, response: Response<RecommendedExercises>) {
                if (response.isSuccessful) {
                    val exercises = response.body()
                } else {
                    // Unsuccessful
                }
            }

            override fun onFailure(call: Call<RecommendedExercises>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }

    // Might be scrapped due to API
    private fun caloriesBurnedCalculation(met: Double, weight: Double, durationHours: Double): Double {
        return met * weight * durationHours
    }
}
