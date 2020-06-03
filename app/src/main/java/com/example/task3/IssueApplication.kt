package com.example.task3

import android.app.Application
import com.example.task3.di.DaggerDatabaseComponent
import com.example.task3.di.DatabaseComponent
import com.example.task3.di.IssueModule

class IssueApplication : Application() {
    lateinit var databaseComponent: DatabaseComponent

    override fun onCreate() {
        super.onCreate()
        databaseComponent = DaggerDatabaseComponent.builder().issueModule(IssueModule(this)).build()
    }
}