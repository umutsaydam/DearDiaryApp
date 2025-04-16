package com.umutsaydam.deardiary.domain.notification

import android.content.Context

interface ReminderScheduler {
    fun scheduleReminder(context: Context, hour: Int, minute: Int, message: String)
    fun cancelReminder(context: Context)
}