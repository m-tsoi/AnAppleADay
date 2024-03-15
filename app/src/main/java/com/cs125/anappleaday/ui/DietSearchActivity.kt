package com.cs125.anappleaday.ui

import android.view.View
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import com.cs125.anappleaday.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.api.ApiMain
import com.google.gson.JsonObject


import com.cs125.anappleaday.data.enumTypes.NutritionData
import com.cs125.anappleaday.data.recycler.DietResultAdapter
import com.cs125.anappleaday.services.auth.FBAuth
import com.cs125.anappleaday.services.firestore.FbDietServices
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DietSearchActivity : AppCompatActivity(), DietResultAdapter.OnAddClickListener  {

    private lateinit var fbAuth: FBAuth
    private lateinit var dietServices: FbDietServices
    private val apiService = ApiMain.getNinjaServices()

    private lateinit var searchView : SearchView
    private lateinit var recyclerResults : RecyclerView
    private lateinit var resultAdapter: DietResultAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_search)

        fbAuth = FBAuth()
        dietServices = FbDietServices(com.google.firebase.Firebase.firestore)

        searchView = findViewById<SearchView>(R.id.searchView)
        recyclerResults = findViewById<RecyclerView>(R.id.recyclerResults)
        resultAdapter = DietResultAdapter(mutableListOf(), this)
        recyclerResults.adapter = resultAdapter

        // listener for search view (edit later)
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
                    val mutableList: MutableList<NutritionData> = nutritionDataList?.toMutableList() ?: mutableListOf()
                    //change to one instead of a list
                    resultAdapter.updateDataSet(mutableList)
                } else {
                    Log.e("DietSearchActivity", "API Request failed with code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<NutritionData>>, t: Throwable) {
                Log.e("DietSearchActivity", "API Request failed", t)
            }
        })
    }

    override fun onAddClick(position: Int) {
        val userId = fbAuth.getUser()?.uid
        if (userId != null) {
            lifecycleScope.launch {
                val dietData = dietServices.getDietData(userId)
                if (dietData != null && dietData.dietScore != null) {
                    // add new thing to dietRecords

                }
            }
        }

    }

}