package com.cs125.anappleaday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.recycler.DietViewAdapter
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbDietServices
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

import java.util.Date


class DietViewActivity : AppCompatActivity() { // displays meals corresponding to current Date

    // firebase but i cant code
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var dietServices: FbDietServices

    // UI components
    private lateinit var recyclerMeals: RecyclerView
    private lateinit var dietViewAdapter: DietViewAdapter
    // could love to have a cute graph but we cant always win in life

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_view)

        recyclerMeals = findViewById<RecyclerView>(R.id.recyclerMeals)

        // initialize firebase
        fbAuth = FBAuth()
        profileServices = FbProfileServices(Firebase.firestore)
        dietServices = FbDietServices(Firebase.firestore)
        personicleServices = FbPersonicleServices(Firebase.firestore)

        //load data
        /*val userId =  fbAuth.getUser()?.uid
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
        }*/

        // to get list of uhhh things we haev to access the firebase
        var tester: MutableList<String> = mutableListOf("cheese")
        tester.add("bread")
        tester.add("milk")

        dietViewAdapter = DietViewAdapter(tester)
        recyclerMeals.adapter = dietViewAdapter

        recyclerMeals.layoutManager = LinearLayoutManager(this)

    }


}