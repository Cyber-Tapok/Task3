package com.example.task3.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.task3.IssueRepository
import com.example.task3.database.IssueDatabase
import com.example.task3.retrofit.REPOS
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(private val application: Application) {

    @Provides
    fun providerViewModelFactory(repository: IssueRepository): ViewModelFactory {
        return ViewModelFactory(repository)
    }

    @Provides
    @Singleton
    fun providesRepository(issueDatabase: IssueDatabase): IssueRepository {
        return IssueRepository(issueDatabase)
    }

    @Provides
    @Singleton
    fun providesDatabase(context: Context): IssueDatabase {
        return Room
            .databaseBuilder(context, IssueDatabase::class.java, REPOS)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun context(): Context {
        return application
    }
}