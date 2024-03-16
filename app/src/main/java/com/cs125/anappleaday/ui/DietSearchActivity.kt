package com.cs125.anappleaday.ui

import android.os.Bundle
import android.util.Log
import com.cs125.anappleaday.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.api.ApiMain
import com.google.gson.JsonObject


import com.cs125.anappleaday.data.enumTypes.NutritionData
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.data.recycler.DietResultAdapter
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbDietServices
import com.cs125.anappleaday.services.firestore.FbHealthPlanServices
import com.cs125.anappleaday.services.firestore.FbPersonicleServices
import com.cs125.anappleaday.services.firestore.FbProfileServices
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DietSearchActivity : AppCompatActivity() {

    //
    private lateinit var fbAuth: FBAuth
    private lateinit var profileServices: FbProfileServices
    private lateinit var personicleServices: FbPersonicleServices
    private lateinit var dietServices: FbDietServices

    private var dietData: DietData? = null

    private lateinit var searchView : SearchView
    private lateinit var recyclerResults : RecyclerView
    private lateinit var dietResultAdapter: DietResultAdapter

    // also add in adapter???

    private val apiService = ApiMain.getNinjaServices()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_search)

        searchView = findViewById<SearchView>(R.id.searchView)
        recyclerResults = findViewById<RecyclerView>(R.id.recyclerResults)
        // add recycler adapter

        // listener for search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { fetchSearchResults(it) }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun fetchSearchResults(query: String) {

        val call = apiService.getNutrition(query)

        call.enqueue(object : Callback<List<NutritionData>> {
            override fun onResponse(call: Call<List<NutritionData>>, response: Response<List<NutritionData>>) {
                if (response.isSuccessful) {
                    val nutritionDataList = response.body()
                    val mutableDataList: MutableList<NutritionData> = nutritionDataList?.toMutableList() ?: mutableListOf()

                    val onAddClickListener: (Int) -> Unit = { position ->
                        var selectedNutritionData = mutableDataList[position]
                        addNutritionDataToFirebase(selectedNutritionData)
                    }

                    dietResultAdapter = DietResultAdapter(mutableDataList,onAddClickListener)
                    recyclerResults.adapter = dietResultAdapter

                } else {
                    Log.e("YourActivity", "API Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<NutritionData>>, t: Throwable) {
                Log.e("YourActivity", "API Request failed", t)
            }
        })
    }

    private fun addNutritionDataToFirebase(nutritionData: NutritionData) {
        // do firebase stuff here
        // add to nutrition, which is Date -> <MutableList>NutritionData
        fbAuth = FBAuth()
        profileServices = FbProfileServices(com.google.firebase.ktx.Firebase.firestore)
        personicleServices = FbPersonicleServices(com.google.firebase.ktx.Firebase.firestore)
        dietServices = FbDietServices(com.google.firebase.ktx.Firebase.firestore)

        val userId =  fbAuth.getUser()?.uid
        if (  userId != null) {
            lifecycleScope.launch {
                val profile = profileServices.getProfile(userId)
                val personicle = personicleServices.getPersonicle(profile?.personicleId!!)
                if (personicle != null) {
                    dietServices.addNutritionData(personicle.dietDataId!!, nutritionData)
                }
            }
        }
    }



    private fun updateUI(response: JsonObject) {
        // Update UI based on the API response

    }


}