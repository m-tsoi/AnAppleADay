package com.cs125.anappleaday.data.sql.repository

import androidx.annotation.WorkerThread
import androidx.room.RoomDatabase
import com.cs125.anappleaday.data.sql.dao.ProfileDao
import com.cs125.anappleaday.data.sql.models.user.Profile

class ProfileRepository (private val profileDao: ProfileDao) {

    @WorkerThread
    suspend fun insert(_profile: Profile): Profile {
        profileDao.insertProfile(_profile)
        return _profile
    }
}