package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.record.models.live.SleepData
import com.cs125.anappleaday.services.auth.FBAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import nl.joery.timerangepicker.TimeRangePicker
import java.util.Date

class HomeActivity : AppCompatActivity() {
    private lateinit var fbAuth: FBAuth
    private lateinit var sleepDataDocRef : DocumentReference
    private lateinit var ls_score : TextView
    private lateinit var sleep_region : ConstraintLayout
    private lateinit var sleep_score : TextView
    private lateinit var diet_region : ConstraintLayout
    private lateinit var diet_score : TextView
    private lateinit var exercise_region : ConstraintLayout
    private lateinit var exercise_score : TextView
    private lateinit var personicle_button : Button
    private lateinit var change_plan_button : Button
    private lateinit var logout_button : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fbAuth = FBAuth()
        val db = Firebase.firestore
        //TODO: get the right document from the right user
        sleepDataDocRef =  db.collection("SleepData").document("rWZTS6CwoL3aq8fnMci8")

        ls_score = findViewById<TextView>(R.id.lifestyle_score)
        sleep_region = findViewById<ConstraintLayout>(R.id.sleep_region)
        sleep_score = findViewById<TextView>(R.id.sleep_score)
        diet_region = findViewById<ConstraintLayout>(R.id.diet_region)
        diet_score = findViewById<TextView>(R.id.diet_score)
        exercise_region = findViewById<ConstraintLayout>(R.id.exercise_region)
        exercise_score = findViewById<TextView>(R.id.exercise_score)
        personicle_button = findViewById<Button>(R.id.personicle_button)
        change_plan_button = findViewById<Button>(R.id.change_plan_button)
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

                // TODO: link to Diet page
                val sendIntent = Intent(this, DietActivity::class.java)
                startActivity(sendIntent)
            }

        // Exercise button functionalities
        exercise_region
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                // TODO: link to Exercise page
//                val sendIntent = Intent(this, SleepActivity::class.java)
//                startActivity(sendIntent)
            }

        // Personicle button functionalities
        personicle_button
            .setOnClickListener{
                Log.d("BUTTONS", "User tapped the login")

                val sendIntent = Intent(this, PersonicleActivity::class.java)
                startActivity(sendIntent)
            }

        // Change plan button functionalities
        change_plan_button
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

        // Initialize UI with data from db
        updateUI()
    }

    fun updateUI() {
        // Update sleep score from db
        sleepDataDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("SLEEP DATA", document.data.toString())
                    val sleepData = document.toObject(SleepData::class.java)!!
                    Log.d("SLEEP DATA", sleepData.dailySleepRecords.toString())

                    val sleepRecordsToday = sleepData.dailySleepRecords.last()
                    if (!isToday(sleepRecordsToday.enteredDate))  {
                        sleep_score.setText("??")
                    } else {
                        sleep_score.setText(sleepRecordsToday.sleepScore.toString())
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

    override fun onStart() {
        super.onStart()
        if (!fbAuth.isCurrentUser()) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
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