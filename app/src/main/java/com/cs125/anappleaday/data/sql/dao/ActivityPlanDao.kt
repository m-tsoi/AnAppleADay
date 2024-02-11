package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.health.ActivityPlan

@Dao
interface ActivityPlanDao {
    @Query("SELECT * FROM ActivityPlan WHERE healthPlanId = :healthPlanId ")
    suspend fun getActivityPlanByHealthPlanId(healthPlanId: String) : ActivityPlan

    @Query("SELECT * FROM ActivityPlan WHERE id = :activityPlanId")
    suspend fun getActivityPlanById(activityPlanId: String) : ActivityPlan

    @Insert
    suspend fun insertActivityPlan(activityPlan: ActivityPlan)

    @Update
    suspend fun updateActivityPlan(activityPlan: ActivityPlan)

    @Delete
    suspend fun deleteActivityPlan(activityPlan: ActivityPlan)

}