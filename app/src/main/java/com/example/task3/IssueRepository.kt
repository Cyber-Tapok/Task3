package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task3.database.IssueDatabase
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue
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


    fun getIssueListFromApi(issueState: IssueState): LiveData<List<GithubIssue>> {
        if (!isRequestStart) {
            isRequestStart = true
            updateDb(issueState)
        }
        return issueList
    }

    fun updateDbWorker() {
        updateDb(IssueState.ALL)
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

    private fun updateDb(issueState: IssueState) {
        val call: Call<List<GithubIssue>> = service.issueCall(USERNAME, REPOS)
        currentStatus.postValue(Status.NONE)
        call.enqueue(object : Callback<List<GithubIssue>> {
            override fun onFailure(call: Call<List<GithubIssue>>, t: Throwable) {
                currentStatus.postValue(Status.FAILED)
                setDbList()
            }

            override fun onResponse(
                call: Call<List<GithubIssue>>,
                response: Response<List<GithubIssue>>
            ) {
                response.body()?.let {
                    issueDatabase.issueDao().resetDb(response.body()!!)
                    currentStatus.postValue(Status.SUCCESS)
                } ?: run {
                    currentStatus.postValue(Status.LIMIT)
                }
                setDbList()
            }

            fun setDbList() {
                if (issueDatabase.issueDao().getAllIssue().isEmpty()) {
                    currentStatus.postValue(Status.EMPTY)
                } else {
                    issueList.postValue(
                        when (issueState) {
                            IssueState.ALL -> issueDatabase.issueDao().getAllIssue()
                            IssueState.OPEN -> issueDatabase.issueDao().getCurrentIssue("open")
                            IssueState.CLOSED -> issueDatabase.issueDao().getCurrentIssue("closed")
                        }
                    )
                }
                isRequestStart = false
            }
        })
    }
}
