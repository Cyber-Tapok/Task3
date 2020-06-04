package com.example.task3.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.task3.database.IssueDao
import com.example.task3.database.IssueDatabase
import com.example.task3.retrofit.GitHubService
import com.example.task3.retrofit.REPOS
import com.example.task3.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class IssueModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesDatabase(context: Context): IssueDao {
        return Room
            .databaseBuilder(context, IssueDatabase::class.java, REPOS)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
            .issueDao()
    }

    @Provides
    @Singleton
    fun providesService(): GitHubService {
        return RetrofitClient().getService()
    }

    @Provides
    fun context(): Context {
        return application
    }
}