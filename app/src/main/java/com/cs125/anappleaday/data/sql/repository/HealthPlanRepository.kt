package com.cs125.anappleaday.data.sql.repository

import androidx.annotation.WorkerThread
import com.cs125.anappleaday.data.sql.dao.HealthPlanDao
import com.cs125.anappleaday.data.sql.models.health.HealthPlan

class HealthPlanRepository(private val healhPlanDao: HealthPlanDao) {
    @WorkerThread
    suspend fun get(id: String): Result<HealthPlan?> {
        return try {
            val profile = healhPlanDao.getHealthPlanById(id)
            Result.success(profile)
        } catch (e: Exception) {
            Result.success(null)
        }
    }

    @WorkerThread
    suspend fun getByProfileId(pid: String): Result<HealthPlan?> {
        return try {
            val profile = healhPlanDao.getHealthPlanByProfileId(pid)
            Result.success(profile)
        } catch (e: Exception) {
            Result.success(null)
        }
    }

    @WorkerThread
    suspend fun insert(_healthPlan: HealthPlan): Result<HealthPlan> {
        return try {
            healhPlanDao.insertHealthPlan(_healthPlan)
            Result.success(_healthPlan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun update(_healthPlan: HealthPlan): Result<HealthPlan> {
        return try {
            healhPlanDao.updateHealthPlan(_healthPlan)
            Result.success(_healthPlan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun delete(id: String): Result<Unit> {
        return try {
            healhPlanDao.deleteHealthPlan(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}