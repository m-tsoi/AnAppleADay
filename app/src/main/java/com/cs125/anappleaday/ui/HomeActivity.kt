package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.live.SleepData
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Date

class HomeActivity : AppCompatActivity() {
    private lateinit var fbAuth: FBAuth
    private lateinit var imageView: ImageView
    private lateinit var sleepDataDocRef : DocumentReference
    private lateinit var ls_score : TextView
    private lateinit var sleep_region : MaterialCardView
    private lateinit var sleep_score : TextView
    private lateinit var diet_region : MaterialCardView
    private lateinit var diet_score : TextView
    private lateinit var exercise_region : MaterialCardView
    private lateinit var exercise_score : TextView
    private lateinit var weight_region: MaterialCardView
    private lateinit var personicle_region : Button
    private lateinit var health_plan_region : Button
    private lateinit var logout_button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fbAuth = FBAuth()


        ls_score = findViewById<TextView>(R.id.lifestyle_score)
        imageView = findViewById<ImageView>(R.id.imageView)
        sleep_region = findViewById<MaterialCardView>(R.id.sleep_region)
        sleep_score = findViewById<TextView>(R.id.sleep_score)
        diet_region = findViewById<MaterialCardView>(R.id.diet_region)
        diet_score = findViewById<TextView>(R.id.diet_score)
        exercise_region = findViewById<MaterialCardView>(R.id.exercise_region)
        exercise_score = findViewById<TextView>(R.id.exercise_score)
        weight_region = findViewById<MaterialCardView>(R.id.weight_region)
        personicle_region = findViewById(R.id.personicle_region)
        health_plan_region = findViewById(R.id.health_plan_region)
        logout_button = findViewById<Button>(R.id.logout_button)

        // Sleep button functionalities
        sleep_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val sendIntent = Intent(this, SleepActivity::class.java)
                startActivity(sendIntent)
            }

        // Diet button functionalities
        diet_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                val sendIntent = Intent(this, DietActivity::class.java)
                startActivity(sendIntent)
            }

        // Exercise button functionalities
        exercise_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                val sendIntent = Intent(this, ExerciseActivity::class.java)
                startActivity(sendIntent)
            }

        weight_region.setOnClickListener{
            Log.d("BUTTONS", "User tapped the login")
            val sendIntent = Intent(this, WeightTrackerActivity::class.java)
            startActivity(sendIntent)
        }

        // Personicle button functionalities
        personicle_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val sendIntent = Intent(this, PersonicleActivity::class.java)
                startActivity(sendIntent)
            }

        // Change plan button functionalities
        health_plan_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                val sendIntent = Intent(this, SelectPlanActivity::class.java)
                startActivity(sendIntent)
            }

        // Logout
        logout_button
            .setOnClickListener{
                fbAuth.logout()
                Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
    }

    override fun onStart() {
        super.onStart()

        // Log the user out if the user is not logged in
        if (!fbAuth.isCurrentUser()) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            val photoUrl = fbAuth.getUser()?.photoUrl
            if (photoUrl != null) {
                imageView.setImageURI(photoUrl)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
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
                        val sleepDataId = personicle.sleepDataId
                        if (sleepDataId != null) {
                            sleepDataDocRef =  db.collection("SleepData").document(sleepDataId)
                            Log.d("BUG", "sleepDataId is not null")

                        } else {
                            Log.d("HomeActivity", "sleepDataId is null")
                        }
                        // TODO: get activityDataId and dietDataId

                        // Initialize UI with data from db
                        updateUI()

                    } else {
                        Log.d("BUG", "personicle is null")
                    }
                } else {
                    Log.d("BUG", "profile or personicleId is null")
                }
                if (!this@HomeActivity::sleepDataDocRef.isInitialized) {
                    Toast.makeText(this@HomeActivity, "Could not get sleepDataId for this user", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Could not get sleepDataId for this user", Toast.LENGTH_SHORT).show()
            Log.d("BUG", "userId is null")
        }
    }

    fun updateUI() {
        // Update sleep score from db
        sleepDataDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("SLEEP DATA", document.data.toString())
                    val sleepData = document.toObject(SleepData::class.java)!!
                    Log.d("SLEEP DATA", sleepData.dailySleepRecords.toString())

                    if (sleepData.dailySleepRecords.isNotEmpty()) {
                        val sleepRecordsToday = sleepData.dailySleepRecords.last()
                        if (isToday(sleepRecordsToday.enteredDate))  {
                            sleep_score.setText(sleepRecordsToday.sleepScore.toString())
                        } else {
                            sleep_score.setText("??")
                        }
                    } else {
                        sleep_score.setText("??")
                    }

                } else {
                    Log.d("SLEEP DATA", "failed :<")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("SLEEP DATA", "get failed with ", exception)
            }

        // TODO: Update diet score from db

        // TODO: Update exercise score from db

        // TODO: Update lifestyle score from db

    }


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