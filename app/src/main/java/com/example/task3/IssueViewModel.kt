package com.example.task3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status

class IssueViewModel(application: Application) : AndroidViewModel(application) {
    private val issueRepository: IssueRepository = IssueRepository()

    fun getAllIssue(): LiveData<List<GithubIssue>> {
        return issueRepository.getIssueListFromApi()
    }
    fun internetStatus(): LiveData<Status> {
        return issueRepository.currentStatus
    }

}