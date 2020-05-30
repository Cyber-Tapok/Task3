package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task3.database.IssueDatabase
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status
import com.example.task3.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IssueRepository(var issueDatabase: IssueDatabase) {
    private val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    private val currentStatus: MutableLiveData<Status> = MutableLiveData()
    private var isRequestStart: Boolean = false


    fun getIssueListFromApi(): LiveData<List<GithubIssue>> {
        val service = RetrofitClient().getService()
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
    fun getFromDb(): LiveData<List<GithubIssue>> {
            issueList.value = issueDatabase.issueDao().getAllIssue()
        return issueList
    }
}
