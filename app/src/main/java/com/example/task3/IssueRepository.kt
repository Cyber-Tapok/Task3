package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class IssueRepository {
    val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    var currentStatus: MutableLiveData<Status> = MutableLiveData()
    var isRequestStart: Boolean = false


    fun getIssueListFromApi(): LiveData<List<GithubIssue>> {
        val builder = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit: Retrofit = builder.build()
        val service = retrofit.create(GitHubService::class.java)
        if (!isRequestStart) {
            isRequestStart = true
            val call: Call<List<GithubIssue>> = service.issueCall("Toinane", "colorpicker")
            currentStatus.value = Status.NONE
            call.enqueue(object : Callback<List<GithubIssue>> {
                override fun onFailure(call: Call<List<GithubIssue>>, t: Throwable) {
                    currentStatus.value = Status.FAILED
                    isRequestStart = false
                }

                override fun onResponse(
                    call: Call<List<GithubIssue>>,
                    response: Response<List<GithubIssue>>
                ) {
                    issueList.value = response.body()!!
                    currentStatus.value = Status.SUCCESS
                    isRequestStart = false
                }
            })
        }
        return issueList
    }
}
