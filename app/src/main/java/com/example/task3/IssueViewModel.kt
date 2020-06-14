package com.example.task3

import androidx.lifecycle.*
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.workManager.UpdateDbWorkerRequest
import kotlinx.coroutines.launch


class IssueViewModel(private val issueRepository: IssueRepository) : ViewModel() {
    private val issueStateLiveData: MutableLiveData<IssueState> = MutableLiveData()

    init {
        issueStateLiveData.value = IssueState.ALL
        UpdateDbWorkerRequest().schedule()
    }

    fun updatedIssues() {
        viewModelScope.launch { issueRepository.updateDb() }
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
