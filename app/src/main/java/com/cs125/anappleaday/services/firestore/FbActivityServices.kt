package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.live.ActivityData
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.data.record.models.live.MealRecord
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbActivityServices (firestore: FirebaseFirestore) : FbBaseServices<ActivityData>(
    "ActivityData", firestore) {
    suspend fun getActivityData(id: String): ActivityData? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<ActivityData>()
        } catch (e: Exception) {
            Log.e(TAG + "Activity", "${e.message}")
            null
        }
    }

//    fun addExerciseRecord(id: String, exercise: HashMap<String, Any>): Task<Void> {
//        return collectionRef.document(id)
//            .update("exercises", FieldValue.arrayUnion(exercise))
//    }
}

