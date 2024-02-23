package com.cs125.anappleaday.services.user

import android.util.Log
import com.cs125.anappleaday.data.enumTypes.ActivityLevel
import com.cs125.anappleaday.data.enumTypes.Gender
import com.cs125.anappleaday.data.sql.models.user.Personicle
import com.cs125.anappleaday.data.sql.repository.PersonicleRepository
import com.cs125.anappleaday.utils.StatCalculator
import java.util.UUID

class PersonicleServices (
    private val personicleRepository: PersonicleRepository) {

    private var personicle: Personicle? = null

    suspend fun get(pid: String) : Personicle? {

        if (personicle?.profileId == pid) {
            return personicle
        }

        val result = personicleRepository.getByProfileId(pid).getOrNull()

        if (result != null) {
            personicle = result
        } else {
            Log.d(TAG, "Failed to retrieve personicle by pid")
        }

        return result
    }

    suspend fun create (
        pid: String, 
        age: Int, 
        gender: Gender, 
        height: Double,
        weight: Double,
        activityLevel: ActivityLevel) : Personicle? {
        
        val bmi = StatCalculator.computeBMI(height, weight)
        val rmr = StatCalculator.computeRMR(age, gender, height, weight)
        val bodyCalories = StatCalculator.computeBodyCalories(rmr, activityLevel)

        val newPersonicle = Personicle(
            id = UUID.randomUUID(),
            healthScore = 0.0,
            profileId = pid,
            height = height,
            weight = weight,
            bmi = bmi,
            rmr = rmr,
            bodyCalories = bodyCalories,
            weightRecordsId = UUID.randomUUID().toString(),
            dietDataId = UUID.randomUUID().toString(),
            activityDataId = UUID.randomUUID().toString(),
            relaxationDataId = UUID.randomUUID().toString(),
            sleepDataId = UUID.randomUUID().toString(),
        )

        if (personicleRepository.insert(newPersonicle).isFailure) {
            Log.d(TAG, "Failed to create personicle")
            return null
        }

        personicle = newPersonicle
        return newPersonicle
    }

    suspend fun update(
        pid: String,
        height: Double?,
        weight: Double?,
        healthScore: Double?
    ) : Personicle? {
        if (personicle == null) {
            val retrievedPersonicle = get(pid)

            if (retrievedPersonicle != null) {
                personicle = retrievedPersonicle
            } else {
                return null
            }
        }


        val newPersonicle = Personicle(
            id = personicle!!.id,
            profileId = personicle!!.profileId,
            healthScore = healthScore ?: personicle!!.healthScore,
            height = height ?: personicle!!.height,
            weight = weight ?: personicle!!.weight,
            bmi = personicle!!.bmi,
            rmr = personicle!!.rmr,
            bodyCalories = personicle!!.rmr,
            weightRecordsId = personicle!!.weightRecordsId,
            dietDataId = personicle!!.dietDataId,
            activityDataId = personicle!!.activityDataId,
            relaxationDataId = personicle!!.relaxationDataId,
            sleepDataId = personicle!!.sleepDataId
        )

        if (personicleRepository.update(newPersonicle).isFailure) {
            Log.d(TAG, "Failed to update personicle")
            return null
        }

        personicle = newPersonicle
        return personicle
    }

    suspend fun delete(pid: String): Boolean {
        if (personicleRepository.delete(pid).isSuccess) {
            personicle = null
            return true
        }

        return false
    }

    companion object {
        private val TAG = "Personicle Services"
    }
}