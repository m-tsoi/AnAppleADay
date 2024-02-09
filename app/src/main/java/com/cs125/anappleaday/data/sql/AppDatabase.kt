package com.cs125.anappleaday.data.sql

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.cs125.anappleaday.data.sql.dao.ActivityPlanDao
import com.cs125.anappleaday.data.sql.dao.DietPlanDao
import com.cs125.anappleaday.data.sql.dao.HealthPlanDao
import com.cs125.anappleaday.data.sql.dao.JobDao
import com.cs125.anappleaday.data.sql.dao.ProfileDao
import com.cs125.anappleaday.data.sql.dao.SleepPlanDao
import com.cs125.anappleaday.data.sql.dao.UserDao
import com.cs125.anappleaday.data.sql.models.health.ActivityPlan
import com.cs125.anappleaday.data.sql.models.health.DietPlan
import com.cs125.anappleaday.data.sql.models.health.HealthPlan
import com.cs125.anappleaday.data.sql.models.health.SleepPlan
import com.cs125.anappleaday.data.sql.models.user.Job
import com.cs125.anappleaday.data.sql.models.user.Profile
import com.cs125.anappleaday.data.sql.models.user.User
import com.cs125.anappleaday.data.sql.views.ActivityPlanView
import com.cs125.anappleaday.data.sql.views.DietPlanView
import com.cs125.anappleaday.data.sql.views.HealthPlanView
import com.cs125.anappleaday.data.sql.views.ProfileView
import com.cs125.anappleaday.data.sql.views.SleepPlanView
import com.cs125.anappleaday.data.sql.views.UserView

@Database(
    entities = [
        User::class,
        Profile::class,
        Job::class,
        HealthPlan::class,
        DietPlan::class,
        ActivityPlan::class,
        SleepPlan::class],
    views = [
        UserView::class,
        ProfileView::class,
        HealthPlanView::class,
        DietPlanView::class,
        ActivityPlanView::class,
        SleepPlanView::class,],
    version = 1,
    autoMigrations = [
        AutoMigration (
            from = 1,
            to = 2,
            spec = AppDatabase.AppsAutoMigration::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun profileDao(): ProfileDao
    abstract fun jobDao(): JobDao
    abstract fun healthPlanDao(): HealthPlanDao
    abstract fun dietPlanDao(): DietPlanDao
    abstract fun activityPlanDao(): ActivityPlanDao
    abstract fun sleepPlanDao(): SleepPlanDao
    class AppsAutoMigration: AutoMigrationSpec {   }
}
