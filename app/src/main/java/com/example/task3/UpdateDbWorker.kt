package com.example.task3

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import javax.inject.Inject

class UpdateDbWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    @Inject
    lateinit var issueRepository: IssueRepository

    override fun doWork(): Result {
        IssueApplication.instance.databaseComponent.inject(this)
        return try {
            issueRepository.updateDbWorker()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}