package com.cs125.anappleaday.ui

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R

import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.api.ApiMain
import com.cs125.anappleaday.data.record.models.healthPlans.ExercisePlan
import com.cs125.anappleaday.data.record.models.live.ActivityData
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbActivityServices
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import kotlinx.coroutines.launch
import java.util.Date

class OldExerciseActivity : AppCompatActivity() {

    // private lateinit var MET: TextView // Metabolic Equivalent of Task
    private lateinit var calories_burned: TextView
    private lateinit var exercise_duration_mins: TextView
    private lateinit var exercise_score: TextView

    //Placeholders until retrieving stored data
    private lateinit var calories_total: TextView // calories_burned +/- calories_total + diet
    private lateinit var recyclerRecommendations: RecyclerView //Helps recycle data already loaded, exercise data

    // Firebase
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var activityServices: FbActivityServices

    // Ninja API
    private val apiService = ApiMain.getNinjaServices()

    // ExercisePlan vars (from data.record.models.healthPlans.ExercisePlan)
    private var exercisePlan: ExercisePlan? = null // recs

    // Buttons
    private lateinit var submit_exercise: Button // Submit exercises as list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.old_activity_exercise)

        fbAuth = FBAuth() // Authorizes Firebase to initialize profile

        // Displayed variables through UI
        // MET = findViewById(R.id.MET)
        calories_burned = findViewById(R.id.calories_burned)
        exercise_duration_mins = findViewById(R.id.exercise_duration)

        // MET, weight, and exercise duration
        // toDoubleOrNull is like ternary statement in JavaScript, return double if double, return null if nondouble
//        val metValue: Double = MET.text.toString().toDoubleOrNull() ?: 0.0
//        val durationHours: Double = exercise_duration_mins.text.toString().toDoubleOrNull() ?: 0.0

        // Calculate calories burned using private function
        //val caloriesBurnedValue: Double = caloriesBurnedCalculation(metValue, personWeight, durationHours)
        //calories_burned.text = "Calories burned: ${caloriesBurnedValue.roundToInt()}"

        calories_burned = findViewById(R.id.calories_burned)
        exercise_duration_mins = findViewById(R.id.exercise_duration)
        submit_exercise = findViewById(R.id.submit_exercise)
        recyclerRecommendations = findViewById(R.id.recycler_recommendations)

        fetchExerciseRecommendations()

        submit_exercise.setOnClickListener {
            // Placeholder for exercise submission logic
            // Replace with actual code for exercise submission
            submitExercise()
        }
    }
    private fun fetchExerciseRecommendations() {
        lifecycleScope.launch {
            try {
                val response = apiService.getRecommendedExercises("param1", "param2", "param3").execute()
                if (response.isSuccessful) {
                    val recommendations = response.body()
                    if (recommendations != null) {
                        // Process recommendations and update UI
                    } else {
                        Log.e("ExerciseActivity", "Empty recommendations response")
                    }
                } else {
                    Log.e("ExerciseActivity", "Failed to fetch exercise recommendations: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ExerciseActivity", "Error fetching exercise recommendations", e)
            }
        }
    }
    private fun submitExercise() {
        // Placeholder for exercise submission logic
        // Replace with actual code for exercise submission
    }

    private fun updateUI() {
        // Placeholder for updating UI with exercise data from Firestore
        // Replace with actual code to update UI
    }
    // If input date == today's date, return score
    // If input date != today's date, return ??
    fun isToday(date: Date?): Boolean {
        val today = Date()
        if (date != null
            && date.day == today.day
            && date.month == today.month
            && date.year == today.year
        ) {
            return true
        } else {
            return false
        }
    }
}
    override fun onResume() {
        super.onResume()

        val db = Firebase.firestore
        val profileServices = FbProfileServices(db)
        val personicleServices = FbPersonicleServices(db)
        val userId = fbAuth.getUser()?.uid
        if (userId != null) {
            lifecycleScope.launch {
                val profile = profileServices.getProfile(userId)
                if (profile != null && profile.personicleId != null) {
                    val personicle = personicleServices.getPersonicle(profile.personicleId)
                    if (personicle != null) {
                        val activityDataId = personicle.activityDataId
                        if (activityDataId != null) {
                            // Retrieve user's exercise data from firebase
                            exerciseDataDocRef = db.collection("SleepData").document(activityDataId)
                            Log.d("BUG", "activityDataId is not null")

                        } else {
                            Log.d("HomeActivity", "activityDataId is null")
                        }

                        // Initialize UI with data from db
                        updateUI()

                    } else {
                        Log.d("BUG", "personicle is null")
                    }
                } else {
                    Log.d("BUG", "profile or personicleId is null")
                }
                if (!this@ExerciseActivity::exerciseDataDocRef.isInitialized) {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Could not get activityDataId for this user",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "Could not get activityDataId for this user", Toast.LENGTH_SHORT)
                .show()
            Log.d("BUG", "userId is null")
        }
    }

    // Update UI with data from db
    fun updateUI() {
        exerciseDataDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("EXERCISE DATA", document.data.toString())
                    val exerciseData = document.toObject(ActivityData::class.java)!!
                    Log.d("EXERCISE DATA", exerciseData.recommendedExercises.toString())

                    if (ActivityData.dailyExerciseRecords.isNotEmpty()) {
                        val exerciseRecordsToday = exerciseData.dailyExerciseRecords.last()
                        if (!isToday(exerciseRecordsToday.enteredDate)) {
                            time_range_confirm.setVisibility(View.VISIBLE)
                            exercise_score.setText("??")
                        } else {


                        } else {
                            Log.d("EXERCISE DATA", "failed :(")
                        }
                    }
                        .addOnFailureListener { exception ->
                            Log.d("EXERCISE DATA", "get failed with ", exception)
                        }
                }

            }
    }

