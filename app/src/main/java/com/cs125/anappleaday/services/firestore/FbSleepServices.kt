package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.live.SleepData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FbSleepServices(firestore: FirebaseFirestore) : FbBaseServices<SleepData>(
    "SleepData", firestore) {
    // Note: add functions if needed

    suspend fun getSleepData(id: String) {

    }

    suspend fun getSleepScore(id: String): Double {
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

    fun createSleepData(id: String, _sleepData: SleepData): Task<Void> {
        return collectionRef.document(id).set(_sleepData)
    }
}