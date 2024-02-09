package com.cs125.anappleaday.data.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cs125.anappleaday.data.sql.models.user.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE uid = :userId")
    suspend fun getUserById(userId: String)

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUserById(userId: String)
}