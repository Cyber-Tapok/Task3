package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task3.database.IssueDao
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue
import com.example.task3.retrofit.GitHubService
import com.example.task3.retrofit.REPOS
import com.example.task3.retrofit.USERNAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class IssueRepository @Inject constructor(
    private val issueDao: IssueDao,
    private val service: GitHubService
) {
    private val currentStatus: MutableLiveData<Status> = MutableLiveData()
    private var isRequestStart: Boolean = false

    fun getByState(issueState: IssueState): LiveData<List<GithubIssue>> {
        return when (issueState) {
            IssueState.ALL -> issueDao.getAllIssues()
            IssueState.OPEN -> issueDao.getIssuesByState("open")
            IssueState.CLOSED -> issueDao.getIssuesByState("closed")
        }
    }

    suspend fun updateDb() {
        if (!isRequestStart) {
            isRequestStart = true
            currentStatus.postValue(Status.NONE)
            withContext(Dispatchers.IO) {
                try {
                    val issueList = service.issueCall(USERNAME, REPOS)
                    if (issueList.isEmpty()) {
                        currentStatus.postValue(Status.EMPTY)
                    } else {
                        currentStatus.postValue(Status.SUCCESS)
                    }
                    issueDao.resetDb(issueList)
                } catch (e: Exception) {
                    currentStatus.postValue(Status.FAILED)
                }
                isRequestStart = false
            }
        }
    }

    fun getCurrentStatus(): LiveData<Status> {
        return currentStatus
    }
}
