package com.example.task3.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.task3.model.GithubIssue

@Dao
interface IssueDao {
    @Transaction
    fun resetDb(issueList: List<GithubIssue>) {
        deleteAll()
        insert(issueList)
    }

    @Query("SELECT * FROM issues ORDER BY number DESC")
    fun getAllIssues(): LiveData<List<GithubIssue>>

    @Query("SELECT * FROM issues WHERE state = :state ORDER BY number DESC")
    fun getIssuesByState(state: String): LiveData<List<GithubIssue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(issueList: List<GithubIssue>)

    @Query("DELETE FROM issues")
    fun deleteAll()
}