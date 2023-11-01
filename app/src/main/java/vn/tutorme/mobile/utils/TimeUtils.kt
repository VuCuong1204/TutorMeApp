package vn.tutorme.mobile.utils

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {
    const val HOUR_MINUTE_FORMAT = "HH:mm"
    const val DAY_MONTH_YEAR_FORMAT = "dd.MM.yyyy"
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val DATE_FORMAT_V2 = "dd-MM-yyyy"

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

    fun calculatorTimeAgo(time: Long?, format: String? = null): String {
        val value = time?.times(1000)
        return if (value != null) {
            val now = Date().time
            var diffTime = now - value

            if (diffTime < 0) diffTime = 0
            val minute = TimeUnit.MILLISECONDS.toMinutes(diffTime)
            val hour = TimeUnit.MILLISECONDS.toHours(diffTime)
            val day = TimeUnit.MILLISECONDS.toDays(diffTime)
            when {
                minute < 1 -> getAppString(R.string.now_ago)
                minute < 60 -> getAppString(R.string.minute_ago, minute)
                hour < 24 -> getAppString(R.string.hour_ago, hour)
                day >= 1 -> convertLongToString(value, format ?: DATE_FORMAT)
                else -> getAppString(R.string.now_ago)
            }
        } else ""
    }

    fun convertLongToString(input: Long, output: String): String {
        val date = Date(input)
        val df2 = SimpleDateFormat(output, Locale.getDefault())
        return df2.format(date)
    }
}
