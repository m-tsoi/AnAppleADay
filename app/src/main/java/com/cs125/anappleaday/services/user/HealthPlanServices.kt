package com.cs125.anappleaday.services.user

import android.provider.ContactsContract.Profile
import android.util.Log
import com.cs125.anappleaday.data.enumTypes.HealthGoal
import com.cs125.anappleaday.data.sql.models.health.HealthPlan
import com.cs125.anappleaday.data.sql.models.user.Personicle
import com.cs125.anappleaday.data.sql.repository.HealthPlanRepository

class HealthPlanServices (private val healthPlanRepository: HealthPlanRepository) {
    private var healthPlan: HealthPlan? = null

    suspend fun getCurrentPlan(id: String, pid: String?, title: String?): HealthPlan? {

        if (healthPlan?.profileId == pid) {
            return healthPlan
        }

        if (healthPlan?.id.toString() == id) {
            return healthPlan
        }

        val result: HealthPlan? = if (pid != null) {
            healthPlanRepository.getByProfileId(pid).getOrNull()
        } else {
            healthPlanRepository.get(id).getOrNull()
        }

        if (result != null) {
                healthPlan = result
        } else {
            Log.d(TAG, "Failed to retrieve personicle by pid")
        }

        return result
    }

    fun createHealhPlan(_profile: Profile, _personicle: Personicle, goal:  HealthGoal) {
        /**
         * TODO: Init 4 plans base on user profile, personicle, healthGoal
         * Diet, Exercise, Relaxation, Sleep
         */
    }

    suspend fun deleteHealPlan(id: String, title: String?): Boolean {
        if (healthPlanRepository.delete(id).isSuccess) {
            healthPlan = null
            return true
        }
        return false
    }

    companion object {
        private val TAG = "HealthPlanServices"
    }
}