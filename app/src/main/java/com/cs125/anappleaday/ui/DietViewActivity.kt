package com.cs125.anappleaday.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.R
import com.cs125.anappleaday.data.enumTypes.ExerciseData
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
        personicleServices = FbPersonicleServices(Firebase.firestore)
        dietServices = FbDietServices(Firebase.firestore)
    }

    override fun onStart(){
        var nutritionDataList = mutableListOf<NutritionData>()

        val userId =  fbAuth.getUser()?.uid
        if (  userId != null) {
            lifecycleScope.launch {
                val profile = profileServices.getProfile(userId)
                val personicle = personicleServices.getPersonicle(profile?.personicleId!!)
                if (personicle != null) {
                    if (personicle.dietDataId != null){
                        val dietData = dietServices.getDietData(personicle?.dietDataId!!)
                        if (dietData != null) {
                            nutritionDataList = dietData.nutrition[Date()]!!
                        }

                        val onDeleteClickListener: (MutableList<NutritionData>) -> Unit = { dataSet ->
                            Log.d("DietViewActivity", "Reset NutritionData: $dataSet")
                            launch {
                                dietServices.resetNutritionData(
                                    personicle.dietDataId,
                                    Date(),
                                    dataSet
                                )
                            }
                        }

                        dietViewAdapter = DietViewAdapter(nutritionDataList,onDeleteClickListener )
                        recyclerMeals.adapter = dietViewAdapter


                    }
                }
            }
        }


        super.onStart()

    }


}