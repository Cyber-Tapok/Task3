package com.example.task3

import androidx.lifecycle.MutableLiveData
import com.example.task3.model.GithubIssue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IssueRepository {
    var issueList: List<GithubIssue> = listOf()
    val liveData: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()


    fun getMutableLiveData(): MutableLiveData<List<GithubIssue>> {
        val builder = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val service = retrofit.create(GitHubService::class.java)

        val call: Call<List<GithubIssue>> = service.reposForuser("Cyber-Tapok")

        call.enqueue(object : Callback<List<GithubIssue>> {
            override fun onFailure(call: Call<List<GithubIssue>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<GithubIssue>>,
                response: Response<List<GithubIssue>>
            ) {
                issueList = response.body()!!
                liveData.value = issueList
            }
        })
        return liveData
    }
}
