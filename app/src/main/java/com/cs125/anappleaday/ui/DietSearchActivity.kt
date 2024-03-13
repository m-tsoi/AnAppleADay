package com.cs125.anappleaday.ui

import android.view.View
import android.os.Bundle
import android.util.Log
import com.cs125.anappleaday.R
import androidx.appcompat.app.AppCompatActivity
import com.cs125.anappleaday.api.ApiMain
import com.google.gson.JsonObject


import com.cs125.anappleaday.data.enumTypes.NutritionData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DietSearchActivity : AppCompatActivity() {

    private val apiService = ApiMain.getAPIServices()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_search)


        val call = apiService.getNutrition("1lb brisket and fries")

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