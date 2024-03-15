package com.cs125.anappleaday.ui

import android.os.Bundle
import android.util.Log
import com.cs125.anappleaday.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.cs125.anappleaday.api.ApiMain
import com.google.gson.JsonObject


import com.cs125.anappleaday.data.enumTypes.NutritionData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DietSearchActivity : AppCompatActivity() {

    private lateinit var searchView : SearchView
    private lateinit var recyclerResults : RecyclerView
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
                    val resultNames = mutableListOf<String>()

                    nutritionDataList?.forEach { nutritionData ->
                        val foodName = nutritionData.name // get names to display
                        resultNames.add(foodName)
                    }


                    // Bind the list of food names to your search bar or any UI component
                    // For example, if you're using an AutoCompleteTextView for the search bar:
                    //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, foodNames)
                    //searchBar.setAdapter(adapter)

                } else {
                    Log.e("YourActivity", "API Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<NutritionData>>, t: Throwable) {
                Log.e("YourActivity", "API Request failed", t)
            }
        })
    }



    private fun updateUI(response: JsonObject) {
        // Update UI based on the API response

    }


}