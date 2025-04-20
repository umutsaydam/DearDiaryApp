package com.umutsaydam.deardiary.presentation.reminder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.notification.ReminderScheduler
import com.umutsaydam.deardiary.domain.useCases.local.reminderNotificationUseCase.IsReminderEnabledUseCase
import com.umutsaydam.deardiary.domain.useCases.local.reminderNotificationUseCase.SetReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val scheduler: ReminderScheduler,
    private val setReminderUseCase: SetReminderUseCase,
    private val isReminderEnabledUseCase: IsReminderEnabledUseCase
) : ViewModel() {

    val isReminderEnabled: Flow<Boolean> = isReminderEnabledUseCase()

    fun scheduleReminder(context: Context, hour: Int, minute: Int) {
        scheduler.scheduleReminder(context, hour, minute, context.getString(R.string.time_to_write_reminder))
    }

    fun setReminder(enabled: Boolean) {
        viewModelScope.launch {
            setReminderUseCase(enabled)
        }
    }

    fun cancelReminder(context: Context){
        scheduler.cancelReminder(context)
    }
}