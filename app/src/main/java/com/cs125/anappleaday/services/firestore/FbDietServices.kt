package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.enumTypes.NutritionData
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.Date

class FbDietServices(firestore: FirebaseFirestore) : FbBaseServices<DietData>(
    "NutritionData", firestore) {
    // Note: add functions if needed

    private val subCollectionName = "NutritionRecord"

    suspend fun getDietData(id: String): DietData? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<DietData>()
        } catch (e: Exception) {
            Log.e(TAG + "DietData", "${e.message}")
            null
        }
    }

    suspend fun addNutritionData(id: String, nutritionData: NutritionData) {
        try {
            val document = collectionRef.document(id).get().await()
            val dietData = document.toObject(DietData::class.java)

            dietData?.let { // if not null
                val currentDate = Date()
                if (it.nutrition.containsKey(currentDate)) {
                    it.nutrition[currentDate]?.add(nutritionData)
                } else {
                    it.nutrition[currentDate] = mutableListOf(nutritionData)
                }
                document.reference.set(it)
            }
        } catch (e: Exception) {
            Log.e(TAG + "AddNutritionData", "${e.message}")
        }
    }

    // gets NutritionData for corresponding date
    suspend fun getPastNutritionData(id: String, date:Date): MutableList<NutritionData> {
        return try {
            val document = collectionRef.document(id).get().await()
            val dietData = document.toObject(DietData::class.java)

            dietData?.let {
                val nutritionForDate = it.nutrition[date]
                nutritionForDate ?: mutableListOf()
            } ?: mutableListOf() // returns nothing if nothing
        } catch (e: Exception) {
            Log.e(TAG + "getPastNutritionData", "${e.message}")
            mutableListOf()
        }

    }
}