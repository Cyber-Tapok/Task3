package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.workManager.UpdateDbWorkerRequest


class IssueViewModel(private val issueRepository: IssueRepository) : ViewModel() {
    private val issueStateLiveData: MutableLiveData<IssueState> = MutableLiveData()


    init {
        issueStateLiveData.value = IssueState.ALL
        UpdateDbWorkerRequest().schedule()
    }

    fun updatedIssues() {
        issueRepository.updateDb()
    }

    val issues = Transformations.switchMap(issueStateLiveData) {
        issueRepository.getByState(it)
    }

    fun setIssueState(state: IssueState) {
        issueStateLiveData.postValue(state)
    }

    fun internetStatus(): LiveData<Status> {
        return issueRepository.getCurrentStatus()
    }
}
