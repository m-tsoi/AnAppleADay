package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.user.Profile

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile WHERE userId = :userId")
    suspend fun getProfileByUserId(userId: String)

    @Insert
    suspend fun insertProfile(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Query("DELETE FROM Profile WHERE userId = :userId")
    suspend fun deleteProfileBy(userId: String)
}