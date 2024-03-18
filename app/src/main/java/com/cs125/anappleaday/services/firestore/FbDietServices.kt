package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.data.record.models.user.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbDietServices(firestore: FirebaseFirestore) : FbBaseServices<DietData>(
    "DietData", firestore) {
    // Note: add functions if needed

    fun createDiet(id: String, _dietData: DietData): Task<Void> {
        return collectionRef.document(id).set(_dietData)
    }

    suspend fun getDietScore(id: String): Double {
        return try {
            val document = collectionRef.document(id)
                .collection("Scores")
                .aggregate(AggregateField.average("score"))
                .get(AggregateSource.SERVER)
                .await()

            if (document != null)
                return document.get(AggregateField.average("score"))?.toDouble()!!

            0.0
        } catch (e: Exception) {
            Log.e(TAG + "Diet", "${e.message}")
            0.0
        }
    }
}