package com.cs125.anappleaday.ui

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.google.gson.JsonObject

import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbDietServices
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch


class DietActivity : AppCompatActivity() {

    // firebase stuff i DEFs understand (ask a trusted adult please)
    private lateinit var fbAuth: FBAuth
    private lateinit var dietServices: FbDietServices

    // UI
    private lateinit var textScore : TextView
    private lateinit var recyclerRecommendations : RecyclerView
    private lateinit var buttonEnter : Button
    private lateinit var buttonView : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)

        // using FBAuth from kotlin+java/com.cs125.anappleaday/services/auth written by competent team member
        fbAuth = FBAuth()
        dietServices = FbDietServices(com.google.firebase.Firebase.firestore)

        // ui
        textScore = findViewById(R.id.textScore)
        recyclerRecommendations = findViewById<RecyclerView>(R.id.recyclerRecommendations)
        buttonEnter = findViewById<Button>(R.id.buttonEnter)
        buttonView = findViewById<Button>(R.id.buttonView)
    }

    override fun onStart() {
        val userId = fbAuth.getUser()?.uid
        if (userId != null) {
            lifecycleScope.launch {
                val dietData = dietServices.getDietData(userId)
                if (dietData != null && dietData.dietScore != null) {
                    textScore.text = dietData.dietScore.toString()

                    var totalCal: Double = 0.0
                    var totalProt: Double = 0.0
                    var totalFat: Double = 0.0
                    var totalCarbs: Int = 0

                    for (nutritionData in dietData.dietRecords) {
                        totalCal += nutritionData.calories
                        totalProt += nutritionData.proteinG
                        totalFat += nutritionData.fatTotalG
                        totalCarbs += nutritionData.carbohydratesTotalG
                    }

                    // get which one is lowest
                    // corresponding amazing high in whatever food
                    // api call for meal
                    // return

                }
            }
        }
        super.onStart()
    }
    // opens search and view respectively
    fun openDietSearch(view: View){
        val intent = Intent(this, DietSearchActivity::class.java)
        startActivity(intent)
    }

    fun openDietView(view: View){
        val intent = Intent(this, DietViewActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI(response: JsonObject) {
        // Update UI based on the API response
        // probably for the uhhhhhhh firebase score and idk suggestions???
    }
}