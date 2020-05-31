package com.example.task3

import android.app.Application
import com.example.task3.di.DaggerDatabaseComponent
import com.example.task3.di.DatabaseComponent
import com.example.task3.di.RoomModule

class IssueApplication : Application() {
    companion object {
        lateinit var instance: IssueApplication
    }

    lateinit var databaseComponent: DatabaseComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        databaseComponent = DaggerDatabaseComponent.builder().roomModule(RoomModule(this)).build()
    }
}