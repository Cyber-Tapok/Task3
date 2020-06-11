package com.example.task3

import androidx.lifecycle.*
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue


class IssueViewModel(private val issueRepository: IssueRepository) : ViewModel() {

    private val issueList: MutableLiveData<List<GithubIssue>> = MutableLiveData<List<GithubIssue>>()
    private val issueStateLiveData: MutableLiveData<IssueState> = MutableLiveData()
    private val dataObserver = Observer<List<GithubIssue>> {
        it ?: return@Observer
        issueList.value = it
    }

    init {
        issueRepository.issueList.observeForever(dataObserver)
        issueStateLiveData.value = IssueState.ALL
    }

    fun updatedIssues() {
        issueRepository.updateDb()
    }

    val issues: LiveData<List<GithubIssue>>
        get() = Transformations.switchMap(issueStateLiveData) {
            issueRepository.getByState(it)
        }

    fun getIssues(state: IssueState) {
        issueStateLiveData.postValue(state)
    }

    override fun onCleared() {
        issueRepository.issueList.removeObserver(dataObserver)
        super.onCleared()
    }

    fun internetStatus(): LiveData<Status> {
        return issueRepository.getCurrentStatus()
    }
}
