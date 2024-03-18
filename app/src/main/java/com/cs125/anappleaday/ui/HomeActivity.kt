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
import com.cs125.anappleaday.data.record.models.user.Personicle
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbExerciseServices
import com.cs125.anappleaday.services.firestore.FbDietServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.cs125.anappleaday.services.firestore.FbSleepServices
import com.cs125.anappleaday.data.record.models.live.SleepData
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Date

class HomeActivity : AppCompatActivity() {
    private lateinit var fbAuth: FBAuth
    private lateinit var imageView: ImageView
    private lateinit var ls_score : TextView
    private lateinit var sleep_region : MaterialCardView
    private lateinit var sleep_score : TextView
    private lateinit var diet_region : MaterialCardView
    private lateinit var diet_score : TextView
    private lateinit var exercise_region : MaterialCardView
    private lateinit var exercise_score : TextView
    private lateinit var personicle_region : Button
    private lateinit var health_plan_region : Button
    private lateinit var logout_button : Button

    private lateinit var sleepDataDocRef : DocumentReference

    // Services
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var dietServices: FbDietServices
    private lateinit var exerciseServices: FbExerciseServices
    private lateinit var sleepServices: FbSleepServices

    private var personicle: Personicle? = null
    private var scoreSet: HashMap<String, Double> = hashMapOf(
        "lifeStyle" to 0.0,
        "diet" to 0.0,
        "exercise" to 0.0,
        "sleep" to 0.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fbAuth = FBAuth()
        val db = Firebase.firestore
        profileServices = FbProfileServices(db)
        personicleServices = FbPersonicleServices(db)
        dietServices = FbDietServices(db)
        exerciseServices = FbExerciseServices(db)
        sleepServices = FbSleepServices(db)

        ls_score = findViewById(R.id.lifestyle_score)
        imageView = findViewById(R.id.imageView)
        sleep_region = findViewById(R.id.sleep_region)
        sleep_score = findViewById(R.id.sleep_score)
        diet_region = findViewById(R.id.diet_region)
        diet_score = findViewById(R.id.diet_score)
        exercise_region = findViewById(R.id.exercise_region)
        exercise_score = findViewById(R.id.exercise_score)
//        weight_region = findViewById(R.id.weight_region)
        personicle_region = findViewById(R.id.personicle_region)
        health_plan_region = findViewById(R.id.health_plan_region)
        logout_button = findViewById(R.id.logout_button)

        // Sleep button functionalities
        sleep_region.setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                startActivity(Intent(this, SleepActivity::class.java))
            }

        // Diet button functionalities
        diet_region.setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                startActivity(Intent(this, DietActivity::class.java))
            }

        // Exercise button functionalities
        exercise_region.setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                startActivity(Intent(this, ExerciseActivity::class.java))
            }

        // Personicle button functionalities
        personicle_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                startActivity(Intent(this, PersonicleActivity::class.java))
            }

        // Change plan button functionalities
        health_plan_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")
                startActivity(Intent(this, SelectPlanActivity::class.java))
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
        loadUserData()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val userId = fbAuth.getUser()?.uid
        lifecycleScope.launch {
            val profile = profileServices.getProfile(userId!!)
            if (profile != null) {
                personicle = personicleServices.getPersonicle(profile.personicleId)

                if (personicle != null) {
                    val dietScore = dietServices.getDietScore(personicle?.dietDataId!!)
                    val exerciseScore = exerciseServices.getExerciseScore(personicle?.exerciseDataId!!)
                    val sleepScore = sleepServices.getSleepScore(personicle?.sleepDataId!!)
                    val lifeStyleScore = (dietScore + exerciseScore + sleepScore) / 3

                    val sleepDataId = personicle!!.sleepDataId
                    if (sleepDataId != null) {
                        val db = Firebase.firestore
                        sleepDataDocRef =  db.collection("SleepData").document(sleepDataId)
                        Log.d("BUG", "sleepDataId is not null")

                    } else {
                        Log.d("HomeActivity", "sleepDataId is null")
                    }

                    if (dietScore == null || exerciseScore == null || sleepScore == null) {
                      Log.d("Something", "failed to load score")
                    } else {
                        Log.d("Diet Score", dietScore.toString())
                        Log.d("Exercise Score", exerciseScore.toString())
                        Log.d("Sleep Score", sleepScore.toString())
                        Log.d("LS Score", lifeStyleScore.toString())

                        scoreSet["lifeStyle"] = lifeStyleScore
                        scoreSet["diet"] = dietScore
                        scoreSet["exercise"] = exerciseScore
                        scoreSet["sleepScore"] = sleepScore
                        updateScoreUI()
                    }


                }
            }
        }
    }

    fun updateScoreUI() {
        // Update sleep score from db
        ls_score.text = String.format("%.0f", scoreSet["lifeStyle"])
        diet_score.text = String.format("%.0f", scoreSet["diet"])
        exercise_score.text = String.format("%.0f", scoreSet["exercise"])
//        sleep_score.text = String.format("%.1f", scoreSet["sleep"])

        sleepDataDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("SLEEP DATA", document.data.toString())
                    val sleepData = document.toObject(SleepData::class.java)!!
                    Log.d("SLEEP DATA", sleepData.dailySleepRecords.toString())

                    if (sleepData.dailySleepRecords.isNotEmpty()) {
                        val sleepRecordsToday = sleepData.dailySleepRecords.last()
                        if (isToday(sleepRecordsToday.enteredDate))  {
                            sleep_score.text = sleepRecordsToday.sleepScore.toString()
                        } else {
                            sleep_score.text = "0.0"
                        }
                    } else {
                        sleep_score.text = "0.0"
                    }

                } else {
                    Log.d("SLEEP DATA", "failed :<")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("SLEEP DATA", "get failed with ", exception)
            }
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