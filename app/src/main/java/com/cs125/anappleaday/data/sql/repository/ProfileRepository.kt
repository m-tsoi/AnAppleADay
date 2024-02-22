package com.cs125.anappleaday.data.sql.repository

import androidx.annotation.WorkerThread
import com.cs125.anappleaday.data.sql.dao.ProfileDao
import com.cs125.anappleaday.data.sql.models.user.Profile
import java.util.UUID

class ProfileRepository (private val profileDao: ProfileDao) {

    @WorkerThread
    suspend fun get(uid: String): Result<Profile?> {
        return try {
            val profile = profileDao.getProfileByUserId(UUID.fromString(uid))
            Result.success(profile)
        } catch (e: Exception) {
            Result.success(null)
        }
    }

    @WorkerThread
    suspend fun insert(_profile: Profile): Result<Profile> {
        return try {
            profileDao.insertProfile(_profile)
            Result.success(_profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun update(_profile: Profile): Result<Profile> {
        return try {
            profileDao.updateProfile(_profile)
            Result.success(_profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @WorkerThread
    suspend fun delete(pid: String): Result<Unit> {
        return try {
            profileDao.deleteProfileBy(UUID.fromString(pid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}