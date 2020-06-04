package com.example.task3.workManager

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class UpdateDbWorkerRequest {
    fun schedule() {
        val workRequest = PeriodicWorkRequest.Builder(
            UpdateDbWorker::class.java,
            15,
            TimeUnit.MINUTES,
            14,
            TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()
        WorkManager.getInstance().enqueue(workRequest)
    }

}