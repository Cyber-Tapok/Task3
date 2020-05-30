package com.example.task3.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.task3.model.GithubIssue

@Database(entities = [GithubIssue::class], version = 1)
abstract class IssueDatabase : RoomDatabase() {
    abstract fun issueDao(): IssueDao
}