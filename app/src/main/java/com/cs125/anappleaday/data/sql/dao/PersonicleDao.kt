package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.user.Personicle

@Dao
interface PersonicleDao {
    @Query("SELECT * FROM Personicle WHERE profileId = :profileId")
    suspend fun getPersonicleByProfileId(profileId: String) : Personicle

    @Query("SELECT * FROM Personicle WHERE id = :personicleId")
    suspend fun getPersonicleById(personicleId: String) : Personicle

    @Insert
    suspend fun insertPersonicle(personicle: Personicle)

    @Update
    suspend fun updatePersonicle(personicle: Personicle)

    @Query("DELETE FROM Personicle WHERE profileId = :profileId")
    suspend fun deletePersonicleByProfileId(profileId: String)

    @Query("DELETE FROM Personicle WHERE id = :personicleId")
    suspend fun deletePersonicleById(personicleId: String)


}