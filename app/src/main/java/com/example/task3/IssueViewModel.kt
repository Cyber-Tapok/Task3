package com.example.task3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.task3.model.GithubIssue

class IssueViewModel(application: Application) : AndroidViewModel(application) {
    private var issueRepository: IssueRepository = IssueRepository()

    fun getAllIssue() : LiveData<List<GithubIssue>>{
        return issueRepository.getMutableLiveData()
    }

}