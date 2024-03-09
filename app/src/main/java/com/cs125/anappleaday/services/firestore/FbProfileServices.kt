package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.record.models.user.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FbProfileServices (firestore: FirebaseFirestore):
    FbBaseServices<Profile>(firestore = firestore, _collectionName = "Profile"){

    suspend fun getProfile(uid: String): Profile? {
        return try {
            val document = collectionRef.document(uid).get().await()
            document.toObject<Profile>()
        } catch (e: Exception){
            Log.e(TAG, "Failed to retrieve profile", e)
            null
        }
    }

    fun createProfile(uid: String, _profile: Profile): Task<Void> {
        return collectionRef.document(uid).set(
            hashMapOf(
                "age" to _profile.age,
                "gender" to _profile.gender.toString(),
                "height" to _profile.height,
                "weight" to _profile.weight,
                "healthPlanId" to _profile.healthPlanId,
                "personicleId" to UUID.randomUUID().toString(),
                "medicalRecords" to mapOf<String, List<String>>(
                    "allergies" to ArrayList(),
                    "foodRestriction" to ArrayList(),
                    "disorders" to ArrayList()
                )
            )
        )
    }

    fun updateProfile(uid: String, dataToUpdate: Map<String, Any?>): Task<Void> {
        return collectionRef.document(uid).update(dataToUpdate)
    }
}