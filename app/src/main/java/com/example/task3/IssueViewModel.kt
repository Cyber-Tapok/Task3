package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.task3.enums.IssueState
import com.example.task3.model.GithubIssue
import com.example.task3.enums.Status


class IssueViewModel(private var issueRepository: IssueRepository) : ViewModel() {

    fun getUpdatedIssues(issueState: IssueState): LiveData<List<GithubIssue>> {
        return issueRepository.getIssueListFromApi(issueState)
    }

    fun getAllIssues(): LiveData<List<GithubIssue>> {
        return issueRepository.getFromDb()
    }

    fun getOpenIssues(): LiveData<List<GithubIssue>> {
        return issueRepository.getCurrentFromDb("open")
    }

    fun getClosedIssues(): LiveData<List<GithubIssue>> {
        return issueRepository.getCurrentFromDb("closed")
    }

    fun internetStatus(): LiveData<Status> {
        return issueRepository.getCurrentStatus()
    }
}