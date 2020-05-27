package com.example.task3.database

import android.app.Application
import androidx.room.Room
class DummyApp : Application() {
    companion object {
        val instance = DummyApp()
    }
    lateinit var database: Database

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, Database::class.java, "database")
            .build()
    }

    fun getInstance(): DummyApp {
        return instance
    }
}