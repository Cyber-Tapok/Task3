package com.example.task3.workManager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.task3.IssueApplication
import com.example.task3.IssueRepository
import javax.inject.Inject

class UpdateDbWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    @Inject
    lateinit var issueRepository: IssueRepository

    override fun doWork(): Result {
        (applicationContext as IssueApplication).databaseComponent.inject(this)
        return try {
            issueRepository.updateDbWorker()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}