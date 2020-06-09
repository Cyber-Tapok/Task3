package com.example.task3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.example.task3.database.IssueDao
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue
import com.example.task3.retrofit.GitHubService
import com.example.task3.retrofit.REPOS
import com.example.task3.retrofit.USERNAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class IssueRepository @Inject constructor(
    val issueDao: IssueDao,
    private val service: GitHubService
) {
    val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    private val currentStatus: MutableLiveData<Status> = MutableLiveData()
    private val issueStateLiveData: MutableLiveData<IssueState> = MutableLiveData()
    private var isRequestStart: Boolean = false
    private val issueListObserver = Observer<List<GithubIssue>> {
        it ?: return@Observer
        issueList.value = it
    }
    private val test = Observer<IssueState> {
        it ?: return@Observer
        Log.e("TEST", it.toString())
    }

    init {
        issueDao.getAllIssues().observeForever(issueListObserver)
        issueStateLiveData.observeForever(test)
    }

    fun getByState(issueState: IssueState) {

//        when (issueState) {
//            IssueState.ALL -> issueDao.getAllIssues().observeForever(issueListObserver)
//            IssueState.OPEN -> issueDao.getIssuesByState("open").observeForever(issueListObserver)
//            IssueState.CLOSED -> issueDao.getIssuesByState("closed").observeForever(issueListObserver)
//        }
    }
    fun updateIssuesState(issueState: IssueState) {
        issueStateLiveData.postValue(issueState)
    }

    fun updateDb() {
        if (!isRequestStart) {
            isRequestStart = true
            val call: Call<List<GithubIssue>> = service.issueCall(USERNAME, REPOS)
            currentStatus.postValue(Status.NONE)
            call.enqueue(object : Callback<List<GithubIssue>> {
                override fun onFailure(call: Call<List<GithubIssue>>, t: Throwable) {
                    currentStatus.postValue(Status.FAILED)
                    isRequestStart = false
                }

                override fun onResponse(
                    call: Call<List<GithubIssue>>,
                    response: Response<List<GithubIssue>>
                ) {
                    response.body()?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (it.isEmpty()) {
                                currentStatus.postValue(Status.EMPTY)
                            }
                            issueDao.resetDb(it)
                        }
                        currentStatus.postValue(Status.SUCCESS)
                    } ?: run {
                        currentStatus.postValue(Status.LIMIT)
                    }
                    isRequestStart = false
                }
            })
        }
    }

    fun getCurrentStatus(): LiveData<Status> {
        return currentStatus
    }
}
