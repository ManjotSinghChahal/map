package com.app.map.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.map.utils.Constants

@Database(entities = [AppEntities::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): AppDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME).build()
    }

}