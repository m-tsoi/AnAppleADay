package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.live.ActivityData
import com.cs125.anappleaday.data.record.models.live.WeightData
import com.cs125.anappleaday.data.record.models.live.WeightRecord
import com.cs125.anappleaday.data.record.models.user.Personicle
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbWeightServices(firestore: FirebaseFirestore) : FbBaseServices<ActivityData>(
    "WeightData", firestore) {
    // Note: add functions if needed

    private val subCollectionName = "WeightRecord"

    suspend fun getWeight(id: String): Personicle? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<Personicle>()
        } catch (e: Exception) {
            Log.e(TAG + "Weight", "${e.message}")
            null
        }
    }

    suspend fun getYesterdayWeightRecord(id: String): WeightRecord? {
        return try {
            val documents = collectionRef.document(id)
                .collection(subCollectionName)
                .whereLessThan("date", FieldValue.serverTimestamp())
                .limit(1)
                .get()
                .await()

            val results = mutableListOf<WeightRecord>()
            for (document in documents)   {
                val weightRecord = document.toObject<WeightRecord>()
                results.add(weightRecord)
            }

            if (results.isEmpty()) throw Exception("Empty documents")
            results[0]
        } catch (e: Exception) {
            Log.e(TAG + "Weight", "${e.message}")
            null
        }
    }

    suspend fun getPastWeightRecords(id:String, nextRecord: WeightRecord? = null): MutableList<WeightRecord> {
        var dateCursor = Timestamp.now()
        if (nextRecord != null)    {
            dateCursor = Timestamp(nextRecord.date)
        }

        return try {
            val documents = collectionRef.document(id)
                .collection(subCollectionName)
                .whereLessThan("date", dateCursor)
                .limit(5)
                .get()
                .await()

            Log.d("check pass documents", documents.toString())

            val weightRecords = mutableListOf<WeightRecord>()
            for (document in documents) {
                val weightRecord = document.toObject<WeightRecord>()
                weightRecords.add(weightRecord)
            }

            weightRecords
        } catch (e: Exception) {
            Log.e(TAG + "Weight", "${e.message}")
            mutableListOf<WeightRecord>()
        }
    }

    fun createWeightData(id: String, _weightData: WeightData): Task<Void> {
       return collectionRef.document(id).set(_weightData)
    }


    fun addWeight(id: String, weight: Int): Task<DocumentReference> {
        return collectionRef.document(id).collection(subCollectionName).add(
            hashMapOf(
                "weight" to weight,
                "date" to FieldValue.serverTimestamp()
            )
        )
    }
}