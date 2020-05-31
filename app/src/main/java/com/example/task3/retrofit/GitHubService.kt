package com.example.task3.retrofit

import com.example.task3.model.GithubIssue
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {
    @GET("/repos/{user}/{repos}/issues?state=all")
    fun issueCall(@Path("user") user: String, @Path("repos") repos: String): Call<List<GithubIssue>>
}