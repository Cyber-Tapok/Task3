package com.example.task3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.task3.model.GithubIssue

class IssueViewModel(application: Application) : AndroidViewModel(application) {
    private val issueRepository: IssueRepository = IssueRepository()

    fun getAllIssue(): LiveData<List<GithubIssue>> {
        return issueRepository.getMutableLiveData()
    }

    var adapter: RecyclerAdapter = RecyclerAdapter()

    init {
        adapter.setHasStableIds(true)
    }

    fun internetStatus(): LiveData<Boolean> {
        return issueRepository.internetConnect
    }

}