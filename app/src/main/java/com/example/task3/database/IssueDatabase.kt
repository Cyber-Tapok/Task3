package com.example.task3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task3.model.GithubIssue
import com.example.task3.retrofit.REPOS

@Database(entities = [GithubIssue::class], version = 3, exportSchema = false)
abstract class IssueDatabase : RoomDatabase() {
    abstract fun issueDao(): IssueDao

    companion object {
        fun start(context: Context): IssueDao {
            return Room.databaseBuilder(context, IssueDatabase::class.java, REPOS)
                .fallbackToDestructiveMigration().build().issueDao()
        }
    }
}