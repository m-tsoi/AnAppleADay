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
import com.cs125.anappleaday.advise.diet.DietAdvisor
import com.cs125.anappleaday.api.ApiMain
import com.cs125.anappleaday.data.record.models.healthPlans.DietPlan
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.data.record.models.live.RecommendedMeal
import com.cs125.anappleaday.data.recycler.DietRecAdapter
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbDietServices
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class DietActivity : AppCompatActivity() {

    // firebase variables
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var healthPlanServices: FbHealthPlanServices
    private lateinit var dietServices: FbDietServices

    // diet variables
    private var dietPlan: DietPlan? = null // for recommendations
    private var dietData: DietData? = null  // for recs+accessing nutrients info
    private var dietAdvisor: DietAdvisor? = null

    // UI components
    private lateinit var textScore : TextView
    private lateinit var recyclerRecommendations : RecyclerView
    private lateinit var dietRecAdapter: DietRecAdapter
    private lateinit var buttonEnter : Button
    private lateinit var buttonView : Button

    // Edamam API
    private val apiService = ApiMain.getEdamamServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)
        // Init firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        healthPlanServices = FbHealthPlanServices(Firebase.firestore)
        dietServices = FbDietServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)

        // init UI components
        textScore = findViewById(R.id.textScore)
        recyclerRecommendations = findViewById<RecyclerView>(R.id.recyclerRecommendations)
        buttonEnter = findViewById<Button>(R.id.buttonEnter)
        buttonView = findViewById<Button>(R.id.buttonView)
    }

    override fun onStart() {
        if (fbAuth.getUser()?.uid  != null) {
            lifecycleScope.launch {
                loadUserData()
            }
        }
        super.onStart()
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
                    dietAdvisor = DietAdvisor(dietPlan!!)
                }
                if (personicle != null) {
                    if (personicle.dietDataId != null) {// causing issues rn cause dietDataId was not there
                        dietData = dietServices.getDietData(personicle.dietDataId!!)

                        // calculate score and recs
                        var score = 0.0
                        var mealRecs = mutableListOf<RecommendedMeal>()
                        // put in dummy data for now
                        mealRecs.add(RecommendedMeal(label = "Chickpea Curry"))
                        mealRecs.add(RecommendedMeal(label = "Lentil Curry"))
                        mealRecs.add(RecommendedMeal(label = "Curry Curry"))

                        if (dietAdvisor != null && dietData != null) {
                            // get prev day
                            var calendar = Calendar.getInstance()
                            calendar.add(Calendar.DATE, -1)

                            val nutrition = dietData!!.nutrition[SimpleDateFormat("M/d/yy").format(Date())]
                            if (nutrition != null) {
                                score = dietAdvisor!!.computeDailyScore(nutrition)
                                // setting rec
                                var exampleNutritionData = nutrition[0]
                                mealRecs = dietAdvisor!!.checkMealAndRecommend(exampleNutritionData)
                            }
                        }


                        // put score in after calculating
                        textScore.text = score.toString()
                        dietServices.addScoreRecord(personicle.dietDataId!!, score)

                        // update/bind recycler
                        dietRecAdapter = DietRecAdapter(mealRecs)
                        recyclerRecommendations.adapter = dietRecAdapter
                    }
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
}