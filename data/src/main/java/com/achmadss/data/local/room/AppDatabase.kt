package com.achmadss.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.achmadss.domain.entity.Contacts

@Database(
    entities = [
        Contacts::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "app_db"
    }

    abstract fun dataDao(): AppDataDao

}