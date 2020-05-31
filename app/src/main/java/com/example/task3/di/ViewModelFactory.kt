package com.example.task3.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.task3.IssueRepository
import com.example.task3.IssueViewModel

class ViewModelFactory(private var issueRepository: IssueRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IssueViewModel(issueRepository) as T
    }
}