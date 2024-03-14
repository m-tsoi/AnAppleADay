package com.cs125.anappleaday.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.R
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class PersonicleActivity : AppCompatActivity() {

    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices

    // UI Components
    private lateinit var heightText: TextView
    private lateinit var weightText: TextView
    private lateinit var bmiText: TextView
    private lateinit var rmrText: TextView
    private lateinit var caloriesBudgetText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personicle)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)

        heightText = findViewById(R.id.numHeight)
        weightText = findViewById(R.id.numWeight)
        bmiText = findViewById(R.id.numBMI)
        rmrText = findViewById(R.id.numRMR)
        caloriesBudgetText = findViewById(R.id.numCaloriesBudget)

        findViewById<Button>(R.id.homeButton).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onStart() {
        val userId = fbAuth.getUser()?.uid
        if (userId != null) {
            lifecycleScope.launch {
                val profile = profileServices.getProfile(userId)
                if (profile != null && profile.personicleId != null) {
                    val personicle = personicleServices.getPersonicle(profile.personicleId)

                    heightText.text = "${formatInches(profile.height)}"
                    weightText.text = "${profile.weight.toString()} lbs"

                    if (personicle != null) {
                        bmiText.text = personicle.bmi.toString()
                        rmrText.text = "${personicle.rmr} (cal/day)"
                        caloriesBudgetText.text = "${personicle.caloriesBudget} (cal/day)"
                    }
                }
            }
        }

        super.onStart()
    }

    private fun formatInches(_inches: Int): String {
        val feet = _inches / 12
        val inches = _inches % 12
        return "${feet}ft. ${inches}in."
    }
}