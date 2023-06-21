package com.samvmisti.bootcounter.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Time {

    private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun convertMillisToString(millis: Long): String {
        val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.US)
        val date = Date(millis)
        return kotlin.runCatching { dateFormat.format(date) }.getOrNull().orEmpty()
    }
}