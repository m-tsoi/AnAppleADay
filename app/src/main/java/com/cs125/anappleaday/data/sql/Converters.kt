package com.cs125.anappleaday.data.sql

import androidx.room.TypeConverter
import com.cs125.anappleaday.data.enumTypes.Gender
import com.cs125.anappleaday.data.enumTypes.HealthGoal
import com.cs125.anappleaday.data.enumTypes.JobType
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromString(value: String?): UUID? {
        return if (value != null) UUID.fromString(value) else null
    }

    @TypeConverter
    fun uuidToString(uuid: UUID?): String? {
        return uuid?.toString()
    }

//    @TypeConverter
//    fun timestampFromDate(date: Date): Long {
//        return date.time
//    }
//
//    @TypeConverter
//    fun dateFromTimestamp(timestamp: Long): Date {
//        return Date(timestamp)
//    }

    @TypeConverter
    fun toGender(value: String) = enumValueOf<Gender>(value)

    @TypeConverter
    fun fromGender(value: Gender) = value.name

    @TypeConverter
    fun toJobType(value: String) = enumValueOf<JobType>(value)

    @TypeConverter
    fun fromJobType(value: JobType) = value.name


    @TypeConverter
    fun toGHealthGoal(value: String) = enumValueOf<HealthGoal>(value)

    @TypeConverter
    fun fromHealthGoal(value: HealthGoal) = value.name
}