package com.cs125.anappleaday.services.firestore

import android.health.connect.datatypes.NutritionRecord
import android.util.Log
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbDietServices(firestore: FirebaseFirestore) : FbBaseServices<DietData>(
    "NutritionData", firestore) {
    // Note: add functions if needed

    private val subCollectionName = "NutritionRecord"

    suspend fun getDietData(id: String): DietData? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<DietData>()
        } catch (e: Exception) {
            Log.e(TAG + "HealthPlan", "${e.message}")
            null
        }
    }

    fun addNutritionRecord(id: String, nutritionRecord: NutritionRecord): Task<DocumentReference> {
        var nutritionRecordMap = nutritionRecord.toMap().toMutableMap()
        nutritionRecordMap["startTime"] = FieldValue.serverTimestamp()
        return collectionRef.document(id).collection(subCollectionName).add(nutritionRecordMap)
    }

    suspend fun getPastNutritionRecord(id: String): MutableList<NutritionRecord> {
        return try {
            val documents = collectionRef.document(id).collection(subCollectionName).whereLessThan("date",
                FieldValue.serverTimestamp()).get().await()

            var nutritionRecords = mutableListOf<NutritionRecord>()
            for (document in documents) {
                var record = document.toObject<NutritionRecord>()
                nutritionRecords.add(record)
            }
            nutritionRecords
        } catch (e: Exception) {
            Log.e(TAG + "HealthPlan", "${e.message}")
            mutableListOf<NutritionRecord>()
        }

    }
}