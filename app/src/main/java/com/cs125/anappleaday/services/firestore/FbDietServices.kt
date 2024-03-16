package com.cs125.anappleaday.services.firestore

import android.health.connect.datatypes.NutritionRecord
import android.util.Log
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

class FbDietServices(firestore: FirebaseFirestore) : FbBaseServices<DietData>(
    "DietData", firestore) {
    // Note: add functions if needed

    private val subCollectionName = "NutritionRecord"

    suspend fun getDietData(id: String): DietData? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<DietData>()
        } catch (e: Exception) {
            Log.e(TAG + "Nutrition Record", "${e.message}")
            null
        }
    }

    suspend fun getPastNutritionRecord(id: String): MutableList<NutritionRecord> {
        return try {
            val documents = collectionRef.document(id).collection(subCollectionName).whereLessThan("date",
                FieldValue.serverTimestamp()).get().await()

            val nutritionRecords = mutableListOf<NutritionRecord>()
            for (document in documents) {
                val record = document.toObject<NutritionRecord>()
                nutritionRecords.add(record)
            }
            nutritionRecords
        } catch (e: Exception) {
            Log.e(TAG + "Nutrition Records", "${e.message}")
            mutableListOf<NutritionRecord>()
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

    fun addNutritionRecord(id: String, nutritionRecord: NutritionRecord): Task<DocumentReference> {
        var nutritionRecordMap = nutritionRecord.toMap().toMutableMap()
        nutritionRecordMap["startTime"] = FieldValue.serverTimestamp()
        return collectionRef.document(id).collection(subCollectionName).add(nutritionRecordMap)
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