package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.health.HealthPlan

@Dao
interface HealthPlanDao {
    @Query("SELECT * FROM HealthPlan")
    suspend fun getAllHealthPlans() : List<HealthPlan>

    @Query("SELECT * FROM HealthPlan WHERE hpId = :healthPlanId")
    suspend fun getHealthPlanById(healthPlanId: String)

    @Query("SELECT * FROM HealthPlan WHERE userId = :userId")
    suspend fun getHealthPlanByUserId(userId: String)

    @Insert
    suspend fun insertHealthPlan(healthPlan: HealthPlan)

    @Update
    suspend fun updateHealthPlan(healthPlan: HealthPlan)

    @Query("DELETE FROM HealthPlan WHERE hpId = :healthPlanId")
    suspend fun deleteHealthPlan(healthPlanId: String)
}