package com.example.studio42.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.studio42.domain.entity.Employer

@Database(
    entities = [Employer::class],
    version = EmployerDatabase.DB_VERSION
)
abstract class EmployerDatabase : RoomDatabase() {

    abstract fun employerDao(): EmployerDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "employer-database"
    }
}