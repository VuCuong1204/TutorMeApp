package vn.tutorme.mobile.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {
    val HOUR_MINUTE_FORMAT = "HH:mm"
    val DAY_MONTH_YEAR_FORMAT = "dd.MM.yyyy"

    fun convertTimeToHour(value: Long): String {
        val date = Date(value * 1000) // Convert seconds to milliseconds

        val timeFormat = SimpleDateFormat(HOUR_MINUTE_FORMAT, Locale.getDefault())

        return timeFormat.format(date)
    }

    fun convertTimeToDay(value: Long): String {
        val date = Date(value * 1000) // Convert seconds to milliseconds

        val timeFormat = SimpleDateFormat(DAY_MONTH_YEAR_FORMAT, Locale.getDefault())

        return timeFormat.format(date)
    }
}
