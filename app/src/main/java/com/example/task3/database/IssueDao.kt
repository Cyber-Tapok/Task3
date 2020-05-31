package com.example.task3.database

import androidx.room.*
import com.example.task3.model.GithubIssue

@Dao
interface IssueDao {
    @Query("SELECT * FROM issues")
    fun getAllIssue(): List<GithubIssue>
    @Query("SELECT * FROM issues WHERE number = :number")
    fun getById(number: Int): GithubIssue
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(issueList: List<GithubIssue>)
}