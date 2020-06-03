package com.example.task3.di

import com.example.task3.MainActivity
import com.example.task3.UpdateDbWorker
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [IssueModule::class])
interface DatabaseComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(updateDbWorker: UpdateDbWorker)
}