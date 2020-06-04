package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task3.database.IssueDao
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue
import com.example.task3.retrofit.GitHubService
import com.example.task3.retrofit.REPOS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

const val USERNAME = "Cyber-Tapok"

@Singleton
class IssueRepository @Inject constructor(
    val issueDao: IssueDao,
    private val service: GitHubService
) {
    private val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    private val currentStatus: MutableLiveData<Status> = MutableLiveData()
    private var isRequestStart: Boolean = false


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
        issueList.value = issueDao.getAllIssues()
        return issueList
    }

    fun getCurrentFromDb(state: String): LiveData<List<GithubIssue>> {
        issueList.value = issueDao.getIssuesByState(state)
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
                    issueDao.resetDb(it)
                    currentStatus.postValue(Status.SUCCESS)
                } ?: run {
                    currentStatus.postValue(Status.LIMIT)
                }
                setDbList()
            }

            fun setDbList() {
                if (issueDao.getAllIssues().isEmpty()) {
                    currentStatus.postValue(Status.EMPTY)
                } else {
                    issueList.postValue(
                        when (issueState) {
                            IssueState.ALL -> issueDao.getAllIssues()
                            IssueState.OPEN -> issueDao.getIssuesByState("open")
                            IssueState.CLOSED -> issueDao.getIssuesByState("closed")
                        }
                    )
                }
                isRequestStart = false
            }
        })
    }
}
