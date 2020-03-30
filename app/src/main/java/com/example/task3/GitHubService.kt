package com.example.task3

import com.example.task3.model.GithubIssue
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {
    @GET("/repos/{user}/TEST/issues")
    fun reposForuser(@Path("user") user: String?): Call<List<GithubIssue>>
}