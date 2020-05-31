package com.example.task3.database

import androidx.room.*
import com.example.task3.model.GithubIssue

@Dao
interface IssueDao {
    @Transaction
    fun resetDb(issueList: List<GithubIssue>) {
        deleteAll()
        insert(issueList)
    }

    @Query("SELECT * FROM issues")
    fun getAllIssue(): List<GithubIssue>

    @Query("SELECT * FROM issues WHERE number = :number")
    fun getById(number: Int): GithubIssue

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(issueList: List<GithubIssue>)

    @Query("DELETE FROM issues")
    fun deleteAll()
}