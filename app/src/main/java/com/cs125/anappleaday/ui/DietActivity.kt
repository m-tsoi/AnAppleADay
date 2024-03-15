package com.cs125.anappleaday.ui

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.google.gson.JsonObject

import androidx.lifecycle.lifecycleScope
import com.cs125.anappleaday.data.record.models.healthPlans.DietPlan
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbDietServices
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import kotlinx.coroutines.launch


class DietActivity : AppCompatActivity() {

    // will add number changing functionality based off API value??? (or from database)
    // will add in adding food eaten that day
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var dietServices: FbDietServices
    private var dietPlan: DietPlan? = null
    private var dietData: DietData? = null

    private lateinit var textScore : TextView
    private lateinit var recyclerRecommendations : RecyclerView
    private lateinit var buttonEnter : Button
    private lateinit var buttonView : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        healthPlanServices = FbHealthPlanServices(Firebase.firestore)
        dietServices = FbDietServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)

        //textDiet = findViewById(R.id.textDiet)
        textScore = findViewById(R.id.textScore)
        recyclerRecommendations = findViewById<RecyclerView>(R.id.recyclerRecommendations)
        buttonEnter = findViewById<Button>(R.id.buttonEnter)
        buttonView = findViewById<Button>(R.id.buttonView)

        // database?
        // get information on previous day's score
        // may also need this to display meals and stuff after entered
    }

    override fun onStart() {
        super.onStart()
        if (fbAuth.getUser()?.uid  != null) {
            lifecycleScope.launch {

            }
        }
    }

    private fun loadUserData() {
        val userId =  fbAuth.getUser()?.uid
        if (  userId != null) {
            lifecycleScope.launch {
                val profile = profileServices.getProfile(userId)
                val healthPlan = healthPlanServices.getHealthPLan(profile?.healthPlanId!!)
                val personicle = personicleServices.getPersonicle(profile?.personicleId!!)
                if (healthPlan != null) {
                    dietPlan = healthPlan.dietPlan
                }
                if (personicle != null) {
                    dietData = dietServices.getDietData(personicle.dietDataId!!)
                }
            }
        }
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

    fun openHomeView(view: View) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI(response: JsonObject) {
        // Update UI based on the API response
        // probably for the uhhhhhhh firebase score and idk suggestions???
    }
}