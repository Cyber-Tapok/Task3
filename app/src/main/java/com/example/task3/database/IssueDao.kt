package com.example.task3.database

import androidx.room.*
import com.example.task3.model.GithubIssue

@Dao
interface IssueDao {
    @Query("SELECT * FROM issues")
    fun getAllIssue(): List<GithubIssue>
    @Query("SELECT * FROM issues WHERE number = :number")
    fun getById(number: Int): GithubIssue
    @Insert
    fun insert(githubIssue: GithubIssue)
    @Delete
    fun delete(githubIssue: GithubIssue)
    @Update
    fun update(githubIssue: GithubIssue)
}