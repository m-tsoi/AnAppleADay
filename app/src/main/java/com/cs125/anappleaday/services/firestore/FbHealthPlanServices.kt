package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.healthPlans.HealthPlan
import com.cs125.anappleaday.data.record.models.user.Profile
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbHealthPlanServices(firestore: FirebaseFirestore):
    FbBaseServices<Profile>(firestore = firestore, _collectionName = "HealhPlan") {

    suspend fun getHealthPLan(id: String): HealthPlan? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<HealthPlan>()
        } catch (e: Exception) {
            Log.e(TAG + "HealthPlan", "${e.message}")
            null
        }
    }

    fun createHealthPlan(_healthPlan: HealthPlan): Task<DocumentReference> {
        return collectionRef.add(
            hashMapOf(
                "healthGoal" to _healthPlan.healthGoal,
                "score" to 0,
                "dietPlan" to _healthPlan.dietPlan.toMap(),
                "exercisePLan" to _healthPlan.exercisePlan.toMap(),
                "sleepPlan" to _healthPlan.sleepPlan.toMap()
            )
        )
    }

    fun update(id: String, dataToUpdate: HashMap<String, Any>): Task<Void> {
        return collectionRef.document(id).update(dataToUpdate)
    }
}