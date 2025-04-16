package com.umutsaydam.deardiary.presentation.reminder

import android.content.Context
import androidx.lifecycle.ViewModel
import com.umutsaydam.deardiary.domain.notification.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val scheduler: ReminderScheduler
) : ViewModel() {
    fun scheduleReminder(context: Context, hour:Int, minute:Int) {
        scheduler.scheduleReminder(context, hour, minute, "Time to write your diary ✍️")
    }
}