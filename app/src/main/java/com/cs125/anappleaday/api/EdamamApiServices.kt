package com.cs125.anappleaday.api

import android.util.Log
import com.cs125.anappleaday.data.enumTypes.MealType
import com.cs125.anappleaday.data.record.models.live.RecommendedMeal
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EdamamApiServices {

    companion object {
        private fun toRecommendedMealObject(_jsonData: JsonElement?): MutableList<RecommendedMeal> {
            val results = mutableListOf<RecommendedMeal>()
            var hits  = JsonArray()
            if (_jsonData != null && _jsonData.isJsonObject) {
                val jsonData = _jsonData.asJsonObject
                hits =  jsonData.get("hits").asJsonArray
            }

            if (hits.size() > 0) {
                for (result in hits) {
                    if (!result.isJsonObject) continue
                    val resultObject = result.asJsonObject
                    val ingredientLines = mutableListOf<String>()
                    resultObject.get("ingredientLines").asJsonArray.forEach {
                            it -> val value = it.asString
                        ingredientLines.add(value)
                    }

                    val nutrientList = resultObject.get("totalNutrients").asJsonObject
                    val proteinG = nutrientList.get("PROCNT").asJsonObject.get("quantity").asDouble
                    val carbG = nutrientList.get("CHOCDF.net").asJsonObject.get("quantity").asDouble
                    val fatG = nutrientList.get("FAT").asJsonObject.get("quantity").asDouble

                    results.add(RecommendedMeal(
                        label = resultObject.get("label").asString,
                        ingredientsLines = ingredientLines,
                        mealType = MealType.valueOf(resultObject.get("mealType").asString),
                        calories = resultObject.get("calories").asDouble,
                        proteinsG = proteinG,
                        carbG = carbG,
                        fatG = fatG,
                        image = resultObject.get("image").asString
                    ))
                }
            }

            return results
        }

        fun recommendRecipes(q: String, calories: Double, mealType: MealType): MutableList<RecommendedMeal> {
            var _result = mutableListOf<RecommendedMeal>()
            val call = ApiMain.getEdamamServices()
                .getRecipes(q = q, calories = calories.toString(), mealType = mealType.toString())
            call.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val results = toRecommendedMealObject(responseBody)
                        _result = results
                    } else {
                        Log.e("YourActivity", "API Request failed with code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Log.e("YourActivity", "API Request failed", t)
                }
            })
            return _result
        }
    }
}

