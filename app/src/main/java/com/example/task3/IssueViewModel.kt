package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status

class IssueViewModel : ViewModel() {
    private val issueRepository: IssueRepository = IssueRepository()

    fun getAllIssue(): LiveData<List<GithubIssue>> {
        return issueRepository.getIssueListFromApi()
    }

    fun internetStatus(): LiveData<Status> {
        return issueRepository.getCurrentStatus()
    }

}