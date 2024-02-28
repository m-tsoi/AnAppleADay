package com.cs125.anappleaday.services.firestore

import android.health.connect.datatypes.ExerciseSessionRecord
import android.util.Log
import com.cs125.anappleaday.data.record.models.ActivityData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Singleton

@Singleton
class FbActivityServices (firestore: FirebaseFirestore) : FbBaseServices<ActivityData>(
    "ActivityData", firestore) {
    // Note: add functions if needed

    private val subCollectionName = "Exercise"

    fun getExercises(dataId: String, lastStartTime: String?): QuerySnapshot? {
        return collectionRef
            .document(dataId)
            .collection(subCollectionName)
            .orderBy("startTime", Query.Direction.DESCENDING)
            .whereLessThan("startTime", true)
            .limit(5)
            .get()
            .addOnSuccessListener {
                Log.d("$TAG $subCollectionName", "Retrieved exercises successfully!")
            }.addOnFailureListener{
                Log.w("$TAG $subCollectionName", "Failed to retrieve exercises")
            }
            .result
    }

    fun addExercise(dataId: String, record: ExerciseSessionRecord) {
        collectionRef
            .document(dataId)
            .collection(subCollectionName)
            .add(record)
            .addOnSuccessListener {
                Log.d("$TAG $subCollectionName", "Added an exercise successfully!")
            }.addOnFailureListener{
                Log.w("$TAG $subCollectionName", "Failed to add the exercise")
            }
    }

    fun updateExercise(dataId: String, recordId: String, record: ExerciseSessionRecord) {
        collectionRef
            .document(dataId)
            .collection(subCollectionName)
            .document(recordId)
            .update(mapOf(
                // TODO: map record fields
            )).addOnSuccessListener {
                Log.d("$TAG $subCollectionName", "Added an exercise successfully!")
            }.addOnFailureListener{
                Log.w(TAG, "Failed to add the exercise")
            }
    }

    fun deleteExercise(dataId: String, recordId: String) {
        collectionRef
            .document(dataId)
            .collection(subCollectionName)
            .document(recordId)
            .delete()
            .addOnSuccessListener {
                Log.d("$TAG $subCollectionName", "Exercise was removed successfully")
            }
            .addOnFailureListener{
                Log.w("$TAG $subCollectionName", "Failed to delete the exercise")
            }
    }
}