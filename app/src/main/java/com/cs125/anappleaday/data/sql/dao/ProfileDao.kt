package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.user.Profile
import com.cs125.anappleaday.data.sql.models.user.ProfileWithHealthPlans
import com.cs125.anappleaday.data.sql.models.user.ProfileWithJobs
import java.util.UUID

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile WHERE pid = :profileId")
    suspend fun getProfileByUserId(profileId: UUID) : Profile

    @Insert
    suspend fun insertProfile(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Query("DELETE FROM Profile WHERE pid = :profileId")
    suspend fun deleteProfileBy(profileId: UUID)

    @Transaction
    @Query("SELECT * FROM Profile")
    suspend fun getProfilesWithHealthPlans(): List<ProfileWithHealthPlans>

    @Transaction
    @Query("SELECT * FROM Profile")
    suspend fun getProfilesWithJobs(): List<ProfileWithJobs>
}