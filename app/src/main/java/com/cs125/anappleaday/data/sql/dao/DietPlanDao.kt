package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.health.DietPlan
import com.cs125.anappleaday.data.sql.models.health.SleepPlan

@Dao
interface DietPlanDao {
    @Query("SELECT * FROM DietPlan WHERE healthPlanId = :healthPlanId")
    suspend fun getDietPlanByHealthPlanId(healthPlanId: String)

    @Query("SELECT * FROM DietPlan WHERE id = :dietPlanId")
    suspend fun getDietPlanById(dietPlanId: String)

    @Insert
    suspend fun insertDietPlan(dietPlan: DietPlan)

    @Update
    suspend fun updateDietPlan(dietPlan: DietPlan)

    @Delete
    suspend fun deleteDietPlan(dietPlan: DietPlan)
}