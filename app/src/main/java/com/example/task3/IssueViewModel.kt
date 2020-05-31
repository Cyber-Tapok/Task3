package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status
import javax.inject.Inject


class IssueViewModel(private var issueRepository: IssueRepository) : ViewModel() {

    fun getAllIssue(): LiveData<List<GithubIssue>> {
        return issueRepository.getIssueListFromApi()
    }
    fun getAllIssueDb(): LiveData<List<GithubIssue>> {
        return issueRepository.getFromDb()
    }

    fun internetStatus(): LiveData<Status> {
        return issueRepository.getCurrentStatus()
    }

}