package com.umutsaydam.deardiary.data.notification

import android.content.Context
import android.os.Build
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.umutsaydam.deardiary.domain.notification.ReminderScheduler
import com.umutsaydam.deardiary.util.ReminderWorker
import java.time.Duration
import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderSchedulerImpl @Inject constructor() : ReminderScheduler {
    override fun scheduleReminder(context: Context, hour: Int, minute: Int, message: String) {
        val nowMillis = System.currentTimeMillis()
        val delayMillis: Long

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = LocalDateTime.now()

            var reminderTime = now
                .withHour(hour)
                .withMinute(minute)
                .withSecond(0)
                .withNano(0)

            if (reminderTime.isBefore(now)) {
                reminderTime = reminderTime.plusDays(1)
            }
            delayMillis = Duration.between(now, reminderTime).toMillis()
        } else {
            val calendarNow = Calendar.getInstance()
            val calendarReminder = Calendar.getInstance()

            calendarReminder.set(Calendar.HOUR_OF_DAY, hour)
            calendarReminder.set(Calendar.MINUTE, minute)
            calendarReminder.set(Calendar.SECOND, 0)

            if (calendarReminder.before(calendarNow)) {
                calendarReminder.add(Calendar.DAY_OF_MONTH, 1)
            }
            delayMillis = calendarReminder.timeInMillis - nowMillis
        }

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("message" to message))
            .addTag("daily_reminder")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    override fun cancelReminder(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("daily_reminder")
    }
}