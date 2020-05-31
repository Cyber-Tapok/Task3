package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task3.database.IssueDatabase
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status
import com.example.task3.retrofit.REPOS
import com.example.task3.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val USERNAME = "Cyber-Tapok"

class IssueRepository(var issueDatabase: IssueDatabase) {
    private val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    private val currentStatus: MutableLiveData<Status> = MutableLiveData()
    private var isRequestStart: Boolean = false
    private val service = RetrofitClient().getService()


    fun getIssueListFromApi(): LiveData<List<GithubIssue>> {
        if (!isRequestStart) {
            isRequestStart = true
            updateDb()
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

    fun getCurrentFromDb(state: String): LiveData<List<GithubIssue>> {
        issueList.value = issueDatabase.issueDao().getCurrentIssue(state)
        return issueList
    }

    fun updateDb() {
        val call: Call<List<GithubIssue>> = service.issueCall(USERNAME, REPOS)
        currentStatus.value = Status.NONE
        call.enqueue(object : Callback<List<GithubIssue>> {
            override fun onFailure(call: Call<List<GithubIssue>>, t: Throwable) {
                currentStatus.value = Status.FAILED
                setDbList()
            }

            override fun onResponse(
                call: Call<List<GithubIssue>>,
                response: Response<List<GithubIssue>>
            ) {
                response.body()?.let {
                    issueDatabase.issueDao().resetDb(response.body()!!)
                    currentStatus.value = Status.SUCCESS
                } ?: run {
                    currentStatus.value = Status.LIMIT
                }
                setDbList()
            }

            fun setDbList() {
                if (issueDatabase.issueDao().getAllIssue().isEmpty()) {
                    currentStatus.value = Status.EMPTY
                } else {
                    issueList.value = issueDatabase.issueDao().getAllIssue()
                }
                isRequestStart = false
            }
        })
    }
}
