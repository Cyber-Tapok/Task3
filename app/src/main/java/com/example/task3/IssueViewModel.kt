package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue


class IssueViewModel(private var issueRepository: IssueRepository) : ViewModel() {

    fun getUpdatedIssues(issueState: IssueState): LiveData<List<GithubIssue>> {
        return issueRepository.getIssueListFromApi(issueState)
    }

    fun getIssues(state: IssueState): LiveData<List<GithubIssue>> {
        return when (state) {
            IssueState.ALL -> issueRepository.getFromDb()
            IssueState.OPEN -> issueRepository.getCurrentFromDb("open")
            IssueState.CLOSED -> issueRepository.getCurrentFromDb("closed")
        }
    }

    fun internetStatus(): LiveData<Status> {
        return issueRepository.getCurrentStatus()
    }
}