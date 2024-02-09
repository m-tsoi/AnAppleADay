package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.user.Job

@Dao
interface JobDao {
    @Query("SELECT * FROM Job WHERE profileId = :profileId")
    suspend fun getJobByProfileId(profileId: String)

    @Insert
    suspend fun insertJob(job: Job)

    @Update
    suspend fun updateJob(job: Job)

    @Delete
    suspend fun deleteJobById(job: Job)
}