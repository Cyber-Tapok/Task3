package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class IssueRepository {
    private val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    private val currentStatus: MutableLiveData<Status> = MutableLiveData()
    private var isRequestStart: Boolean = false


    fun getIssueListFromApi(): LiveData<List<GithubIssue>> {
        val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            val builder: Request.Builder = originalRequest.newBuilder().header(
                "Authorization",
                Credentials.basic("Cyber-Tapok", "cdLE6fb3drLEnBH")
            )
            val newRequest: Request = builder.build()
            chain.proceed(newRequest)
        }.build()
        val builder = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit: Retrofit = builder.build()
        val service = retrofit.create(GitHubService::class.java)
        if (!isRequestStart) {
            isRequestStart = true
            val call: Call<List<GithubIssue>> = service.issueCall("Cyber-Tapok", "TEST")
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
                    response.body()?.let {
                        issueList.value = response.body()
                        currentStatus.value = Status.SUCCESS
                    } ?: run {
                        issueList.value = emptyList()
                        currentStatus.value = Status.LIMIT
                    }
                    isRequestStart = false
                }
            })
        }
        return issueList
    }

    fun getCurrentStatus(): LiveData<Status> {
        return currentStatus
    }
}
