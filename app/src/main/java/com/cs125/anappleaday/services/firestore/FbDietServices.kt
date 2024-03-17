package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.enumTypes.NutritionData
import com.cs125.anappleaday.data.record.models.healthPlans.HealthPlan
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.Date

class FbDietServices(firestore: FirebaseFirestore) : FbBaseServices<DietData>(
    "DietData", firestore) {
    // Note: add functions if needed

    private val subCollectionName = "DietData"

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

    suspend fun resetNutritionData(id: String, date: Date, nutritionDataList: MutableList<NutritionData>) {
        try {   // this is to straight up resent NutritionData list
            val document = collectionRef.document(id).get().await()
            val dietData = document.toObject(DietData::class.java)

            dietData?.let { // if not null
                it.nutrition[date] = nutritionDataList  // sets date to list
                document.reference.set(it)
            }
        } catch (e: Exception) {
            Log.e(TAG + "ResetNutritionData", "${e.message}")
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

    suspend fun getAverageScore(id: String): Double? {
        return try {
            val avgResult = collectionRef.document(id)
                .collection("Scores")
                .whereLessThan("date", FieldValue.serverTimestamp())
                .aggregate(AggregateField.average("score"))
                .get(AggregateSource.SERVER)
                .await()

            avgResult?.let {
                val averageScore = it.getDouble(AggregateField.average("score")) // Extracting the average score
                averageScore
            }
        } catch (e: Exception) {
            Log.e(TAG + "Diet average score", "${e.message}")
            null
        }
    }

    fun addScoreRecord(id: String, score: Double): Task<DocumentReference> {
         return collectionRef.document(id).collection("Scores").add(
             hashMapOf(
                 "score" to score,
                 "date" to FieldValue.serverTimestamp()
             )
         )
    }
}