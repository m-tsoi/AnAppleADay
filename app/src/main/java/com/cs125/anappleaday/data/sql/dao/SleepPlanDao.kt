package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.health.SleepPlan

@Dao
interface SleepPlanDao {
    @Query("SELECT * FROM SleepPlan WHERE healthPlanId = :healthPlanId ")
    suspend fun getSleepPlanByHealthPlanId(healthPlanId: String) : SleepPlan

    @Query("SELECT * FROM SleepPlan WHERE id= :sleepPlanId")
    suspend fun getSleepPlanById(sleepPlanId: String) : SleepPlan

    @Insert
    suspend fun insertSleepPlan(sleepPlan: SleepPlan)

    @Update
    suspend fun updateSleepPlan(sleepPlan: SleepPlan)

    @Delete
    suspend fun deleteSleepPlan(sleepPlan: SleepPlan)
}