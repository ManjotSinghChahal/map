package com.app.map.baseClasses

import android.app.Application
import androidx.room.Room
import com.app.map.data.local.AppDatabase
import com.app.map.utils.Constants

class App : Application() {

    companion object {
        lateinit var instance: App private set
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createDB()
    }

    private fun createDB() {
        db = Room.databaseBuilder(instance, AppDatabase::class.java,Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}