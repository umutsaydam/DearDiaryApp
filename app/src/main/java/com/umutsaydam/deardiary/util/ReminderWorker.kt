package com.umutsaydam.deardiary.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.umutsaydam.deardiary.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val message = inputData.getString("message") ?: return Result.failure()

        // Basit Notification gösterimi
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Reminder")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification_outline)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(15, notification)

        return Result.success()
    }
}