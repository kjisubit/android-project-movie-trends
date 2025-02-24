package com.js.movietrends.ui.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtil {
    fun getTodayDate(): String {
        val today = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(today.time)
    }

    fun getOneWeekAgoDate(): String {
        val weekAgo = Calendar.getInstance()
        weekAgo.add(Calendar.WEEK_OF_YEAR, -1) // 일주일 전으로 설정
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(weekAgo.time)
    }
}