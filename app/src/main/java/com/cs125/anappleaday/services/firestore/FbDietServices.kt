package com.cs125.anappleaday.services.firestore

import android.util.Log
import com.cs125.anappleaday.data.enumTypes.NutritionData
import com.cs125.anappleaday.data.record.models.live.DietData
import com.cs125.anappleaday.utils.toMap
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FbDietServices(firestore: FirebaseFirestore) :
    FbBaseServices<DietData>("NutritionData", firestore) {
    suspend fun getDietData(id: String): DietData? {
        return try {
            val document = super.getDocument(id).await()
            document.toObject<DietData>()
        } catch (e: Exception) {
            Log.e(TAG + "Failed to retrieve DietData", "${e.message}")
            null
        }
    }

    fun createDietData(_dietData: DietData): Task<DocumentReference> {
        val dietDataMap = hashMapOf(
            "dietScore" to _dietData.dietScore,
            "dietRecords" to _dietData.dietRecords
        )

        // add map to firestore
        return collectionRef.add(dietDataMap)
    }

    //modify
    fun addToDietRecords(id: String, newItem: NutritionData): Task<Void> {
        return try {
            val documentRef = firestore.collection(collectionName).document(id)

            // Get the current DietData object from Firestore
            val currentDietData = getDietData(id)

            // If the document exists, update the dietRecords field
            if (currentDietData != null) {
                currentDietData.dietRecords.add(newItem)
                val dataToUpdate = mapOf("dietRecords" to currentDietData.dietRecords)
                documentRef.update(dataToUpdate)
            } else {
                // Handle the case where the document doesn't exist
                Log.e(TAG, "DietData document does not exist for ID: $id")
                throw IllegalStateException("DietData document does not exist for ID: $id")
            }
        } catch (e: Exception) {
            // Handle any exceptions
            Log.e(TAG, "Error adding item to dietRecords: ${e.message}")
            throw e
        }
    }

    fun update(id: String, dataToUpdate: Map<String, Any>): Task<Void> {
        return collectionRef.document(id).update(dataToUpdate)
    }

}