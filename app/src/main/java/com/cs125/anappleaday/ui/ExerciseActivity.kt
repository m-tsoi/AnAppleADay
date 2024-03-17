package com.cs125.anappleaday.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.cs125.anappleaday.api.ApiMain
import com.cs125.anappleaday.data.record.models.live.ActivityData
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt
class ExerciseActivity : AppCompatActivity() {

    private lateinit var MET: TextView // Metabolic Equivalent of Task
    private lateinit var calories_burned: TextView
    private lateinit var exercise_duration_mins: TextView
    private lateinit var exercise_score : TextView
    //Placeholders until retrieving stored data
    private lateinit var calories_total: TextView // calories_burned +/- calories_total + diet
    private lateinit var recyclerRecommendations : RecyclerView //Helps recycle data already loaded, exercise data

    // API
    private lateinit var fbAuth: FBAuth
    private lateinit var exerciseDataDocRef : DocumentReference
    private lateinit var autoCompleteTextViewExercise: AutoCompleteTextView
    private lateinit var exerciseAdapter: ArrayAdapter<String>

    // Buttons
    private lateinit var submit_exercise : Button // Submit exercises as list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        fbAuth = FBAuth() // Authorizes Firebase to initialize profile

        // Displayed variables through UI
        MET = findViewById(R.id.MET)
        calories_burned = findViewById(R.id.calories_burned)
        exercise_duration_mins = findViewById(R.id.exercise_duration)

        // MET, weight, and exercise duration
        // toDoubleOrNull is like ternary statement in JavaScript, return double if double, return null if nondouble
        val metValue: Double = MET.text.toString().toDoubleOrNull() ?: 0.0
        val durationHours: Double = exercise_duration_mins.text.toString().toDoubleOrNull() ?: 0.0

        // Calculate calories burned using private function
        //val caloriesBurnedValue: Double = caloriesBurnedCalculation(metValue, personWeight, durationHours)
        //calories_burned.text = "Calories burned: ${caloriesBurnedValue.roundToInt()}"

        // Make API call to NinjaAPI
        val apiServices = ApiMain.getNinjaServices()
        val call = apiServices.getRecommendedExercises(param1, param2, param3) //Edit params
        call.enqueue(object : Callback<ActivityData.recommendedExercises> {
            override fun onResponse(call: Call<recommendedExercises>, response: Response<RecommendedExercises>) {
                if (response.isSuccessful) {
                    val exerciseDataList = response.body()
                    val exerciseResultNames = mutableListOf<String>()

                    exerciseDataList?.forEach{ exerciseDate}
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
                            exerciseDataDocRef =  db.collection("SleepData").document(activityDataId)
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
                    Toast.makeText(this@ExerciseActivity, "Could not get activityDataId for this user", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Could not get activityDataId for this user", Toast.LENGTH_SHORT).show()
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
                        if (!isToday(exerciseRecordsToday.enteredDate))  {
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
    // Might be scrapped due to API
    fun caloriesBurnedCalculation(met: Double, weight: Double, durationHours: Double): Double {
        return met * weight * durationHours
    }
    // If input date == today's date, return score
    // If input date != today's date, return ??
    fun isToday(date: Date?): Boolean {
        val today = Date()
        if (date != null
            && date.day == today.day
            && date.month == today.month
            && date.year == today.year) {
            return true
        } else {
            return false
        }
    }
}
