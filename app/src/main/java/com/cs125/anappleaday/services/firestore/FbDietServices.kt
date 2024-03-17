package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.data.record.models.user.Personicle
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbDietServices(firestore: FirebaseFirestore) : FbBaseServices<DietData>(
    "NutritionData", firestore) {
    // Note: add functions if needed

    suspend fun getDietData(id: String): DietData? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<DietData>()
        } catch (e: Exception) {
            Log.e(TAG + "Diet", "${e.message}")
            null
        }
    }

    // Temporary function
//    fun addMealRecord(id: String, mealRecord: MealRecord): Task<Void> {
//        return collectionRef.document(id)
//            .update("meals", FieldValue.arrayUnion(mealRecord.toMap()))
//    }
}