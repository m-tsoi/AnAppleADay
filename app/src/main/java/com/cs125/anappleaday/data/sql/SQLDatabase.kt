package com.cs125.anappleaday.data.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cs125.anappleaday.data.sql.dao.ActivityPlanDao
import com.cs125.anappleaday.data.sql.dao.DietPlanDao
import com.cs125.anappleaday.data.sql.dao.HealthPlanDao
import com.cs125.anappleaday.data.sql.dao.JobDao
import com.cs125.anappleaday.data.sql.dao.PersonicleDao
import com.cs125.anappleaday.data.sql.dao.ProfileDao
import com.cs125.anappleaday.data.sql.dao.SleepPlanDao
import com.cs125.anappleaday.data.sql.models.health.ActivityPlan
import com.cs125.anappleaday.data.sql.models.health.DietPlan
import com.cs125.anappleaday.data.sql.models.health.HealthPlan
import com.cs125.anappleaday.data.sql.models.health.SleepPlan
import com.cs125.anappleaday.data.sql.models.user.Job
import com.cs125.anappleaday.data.sql.models.user.Personicle
import com.cs125.anappleaday.data.sql.models.user.Profile
import com.cs125.anappleaday.data.sql.views.ActivityPlanView
import com.cs125.anappleaday.data.sql.views.DietPlanView
import com.cs125.anappleaday.data.sql.views.HealthPlanView
import com.cs125.anappleaday.data.sql.views.PersonicleView
import com.cs125.anappleaday.data.sql.views.ProfileView
import com.cs125.anappleaday.data.sql.views.SleepPlanView

@Database(
    entities = [
        Profile::class,
        Personicle::class,
        Job::class,
        HealthPlan::class,
        DietPlan::class,
        ActivityPlan::class,
        SleepPlan::class],
    views = [
        ProfileView::class,
        PersonicleView::class,
        HealthPlanView::class,
        DietPlanView::class,
        ActivityPlanView::class,
        SleepPlanView::class,],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class SQLDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun personicleDao(): PersonicleDao
    abstract fun jobDao(): JobDao
    abstract fun healthPlanDao(): HealthPlanDao
    abstract fun dietPlanDao(): DietPlanDao
    abstract fun activityPlanDao(): ActivityPlanDao
    abstract fun sleepPlanDao(): SleepPlanDao

    companion object {

        @Volatile
        private var INSTANCE: SQLDatabase? = null

        fun getDatabase(context: Context): SQLDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SQLDatabase::class.java,
                    "anappleaday_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

