package com.cs125.anappleaday.ui

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.google.gson.JsonObject

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


class ExerciseActivity2 : AppCompatActivity() {

    // firebase variables
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var activityServices: FbActivityServices

    // ExercisePlan vars (from data.record.models.healthPlans.ExercisePlan)
    private var exercisePlan: ExercisePlan? = null // recs
    // TODO: Ask if activityData can be renamed to exerciseData
    private var exerciseData: ActivityData? = null

    // UI components
    private lateinit var textScore : TextView
    private lateinit var recyclerRecommendations : RecyclerView
    private lateinit var buttonEnter : Button
    private lateinit var buttonView : Button

    // Ninja API
    private val apiService = ApiMain.getNinjaServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        healthPlanServices = FbHealthPlanServices(Firebase.firestore)
        activityServices = FbActivityServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)

        // init UI components
        textScore = findViewById(R.id.textScore)
        recyclerRecommendations = findViewById<RecyclerView>(R.id.recyclerRecommendations)
        buttonEnter = findViewById<Button>(R.id.buttonEnter)
        buttonView = findViewById<Button>(R.id.buttonView)
    }

    override fun onStart() {
        super.onStart()
        if (fbAuth.getUser()?.uid  != null) {
            lifecycleScope.launch {
                loadUserData()
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
                    exercisePlan = healthPlan.exercisePlan
                }
                if (personicle != null) {
                    if (personicle.activityDataId != null)
                        exerciseData = activityServices.getActivityData(personicle.activityDataId!!)
                }
            }
        }
    }

    private fun getRecommendations(){

        // adjust parameters
        val call = apiService.getRecipes(q ="star")


    }

    // opens search and view respectively
    fun openDietSearch(view: View){
        val intent = Intent(this, ExerciseSearchActivity1::class.java)
        startActivity(intent)
    }

    fun openDietView(view: View){
        val intent = Intent(this, ExerciseViewActivity::class.java)
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