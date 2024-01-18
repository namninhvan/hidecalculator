package com.techbmt.hidecalculator.core

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.floor

fun getCurrentDateTime(): String {
    val calendar = Calendar.getInstance()
    val currentDate = calendar.time

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormatter.format(currentDate)
}

fun convertDuration(duration: Long): String {
    val minute: Long = (duration / 1000) / 60
    val second: Long = (duration / 1000) % 60
    if(minute < 10 && second < 10) {
        return "0$minute:0$second"
    } else if(minute < 10 && second > 10) {
        return "0$minute:$second"
    } else if(minute > 10 && second > 10) {
        return "$minute:$second"
    } else if(minute > 10 && second < 10) {
        return "$minute:0$second"
    }
    return ""
}