package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue


class IssueViewModel(private var issueRepository: IssueRepository) : ViewModel() {

    private val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    private val dataObserver = Observer<List<GithubIssue>> {
        it ?: return@Observer
        issueList.value = it
    }

    init {
        issueRepository.issueList.observeForever(dataObserver)
    }

    fun updatedIssues() {
        issueRepository.updateDb()
    }

    val issues: LiveData<List<GithubIssue>> get() = issueList

    fun getIssues(state: IssueState) {
        issueRepository.getByState(state)
    }

    override fun onCleared() {
        issueRepository.issueList.observeForever(dataObserver)
        super.onCleared()
    }

    fun internetStatus(): LiveData<Status> {
        return issueRepository.getCurrentStatus()
    }
}