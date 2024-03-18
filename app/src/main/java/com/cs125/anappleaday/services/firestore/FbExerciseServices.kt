package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.live.ExerciseData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbExerciseServices (firestore: FirebaseFirestore) : FbBaseServices<ExerciseData>(
    "ExerciseData", firestore) {
    suspend fun getExerciseData(id: String): ExerciseData? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<ExerciseData>()
        } catch (e: Exception) {
            Log.e(TAG + "Exercise", "${e.message}")
            null
        }
    }

    fun createExerciseData(id: String, _exerciseData: ExerciseData): Task<Void> {
        return collectionRef.document(id).set(_exerciseData)
    }

    suspend fun getExerciseScore(id: String): Double {
        return try {
            val document = collectionRef.document(id)
                .collection("Scores")
                .aggregate(AggregateField.average("score"))
                .get(AggregateSource.SERVER)
                .await()

            Log.d("Exer Score", document.toString())

            if (document != null)
                return document.get(AggregateField.average("score"))?.toDouble()!!

            0.0
        } catch (e: Exception) {
            Log.e(TAG + "Diet", "${e.message}")
            0.0
        }
    }
}