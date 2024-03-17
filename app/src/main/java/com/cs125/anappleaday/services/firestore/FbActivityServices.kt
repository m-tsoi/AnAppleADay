package com.cs125.anappleaday.services.firestore

import android.health.connect.datatypes.ExerciseSessionRecord
import android.util.Log
import com.cs125.anappleaday.data.record.models.live.ActivityData
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.AggregateField
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbActivityServices (firestore: FirebaseFirestore) : FbBaseServices<ActivityData>(
    "ActivityData", firestore) {
    // Note: add functions if needed

    private val subCollectionName = "ExerciseSessionRecord"

    suspend fun getExerciseData(id: String): ExerciseSessionRecord? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<ExerciseSessionRecord>()
        } catch (e: Exception) {
            Log.e(TAG + "Exercise", "${e.message}")
            null
        }
    }

    suspend fun getPastExerciseRecords(id: String): MutableList<ExerciseSessionRecord> {
        return try {
            val documents = collectionRef.document(id).collection(subCollectionName).whereLessThan("date",
                FieldValue.serverTimestamp()).get().await()

            var nutritionRecords = mutableListOf<ExerciseSessionRecord>()
            for (document in documents) {
                var record = document.toObject<ExerciseSessionRecord>()
                nutritionRecords.add(record)
            }
            nutritionRecords
        } catch (e: Exception) {
            Log.e(TAG + "Exercise", "${e.message}")
            mutableListOf<ExerciseSessionRecord>()
        }
    }

    fun addExerciseSessionRecordRecord(id: String, exerciseSessionRecord: ExerciseSessionRecord): Task<DocumentReference> {
        var nutritionRecordMap = exerciseSessionRecord.toMap().toMutableMap()
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
            Log.e(TAG + "Exercise average score", "${e.message}")
            null
        }
    }
}