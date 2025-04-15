package com.umutsaydam.deardiary.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    fun formatForUi(date: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val targetFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val parsedDate = originalFormat.parse(date)
        return targetFormat.format(parsedDate!!)
    }

    fun formatForSelectedDateForServer(dateMillis: Long): String {
        val date = Date(dateMillis)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatForSelectedDate(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd-MMM-yyy", Locale.getDefault())
        return dateFormat.format(millis)
    }
}