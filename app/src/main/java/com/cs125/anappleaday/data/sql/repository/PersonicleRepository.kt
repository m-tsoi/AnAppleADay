package com.cs125.anappleaday.data.sql.repository

import androidx.annotation.WorkerThread
import com.cs125.anappleaday.data.sql.dao.PersonicleDao
import com.cs125.anappleaday.data.sql.models.user.Personicle

class PersonicleRepository(private val personicleDao: PersonicleDao) {
    @WorkerThread
    suspend fun get(id: String): Result<Personicle?> {
        return try {
            val personicle = personicleDao.getPersonicleById(id)
            Result.success(personicle)
        } catch (e: Exception) {
            Result.success(null)
        }
    }

    @WorkerThread
    suspend fun getByProfileId(pid: String): Result<Personicle?> {
        return try {
            val personicle = personicleDao.getPersonicleByProfileId(pid)
            Result.success(personicle)
        } catch (e: Exception) {
            Result.success(null)
        }
    }

    @WorkerThread
    suspend fun insert(_personicle: Personicle): Result<Personicle> {
        return try {
            personicleDao.insertPersonicle(_personicle)
            Result.success(_personicle)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun update(_personicle: Personicle): Result<Personicle> {
        return try {
            personicleDao.updatePersonicle(_personicle)
            Result.success(_personicle)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun delete(pid: String): Result<Unit> {
        return try {
            personicleDao.deletePersonicleById(pid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}