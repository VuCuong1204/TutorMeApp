package vn.tutorme.mobile.utils

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {
    const val HOUR_MINUTE_FORMAT = "HH:mm:ss"
    const val HOUR_MINUTE_FORMAT_V1 = "HH:mm"
    const val DAY_MONTH_YEAR_FORMAT = "dd.MM.yyyy"
    const val DATE_FORMAT = "dd/MM/yyyy"
    const val DATE_FORMAT_V2 = "dd-MM-yyyy"

    fun convertTimeToHour(value: Long, format: String = HOUR_MINUTE_FORMAT): String {
        val date = Date(value * 1000) // Convert seconds to milliseconds

        val timeFormat = SimpleDateFormat(format, Locale.getDefault())

        return timeFormat.format(date)
    }

    fun convertTimeToDay(value: Long, type: String = DAY_MONTH_YEAR_FORMAT): String {
        val date = Date(value * 1000) // Convert seconds to milliseconds

        val timeFormat = SimpleDateFormat(type, Locale.getDefault())

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

    fun convertTimeToHourMinutes(value: Long): String {
        val hour = value / 3600
        val minutes = (value % 3600) / 60
        val second = (value % 3600) % 60

        return String.format(getAppString(R.string.hour_minutes_format), hour, minutes)
    }

    fun convertLongToString(input: Long, output: String): String {
        val date = Date(input)
        val df2 = SimpleDateFormat(output, Locale.getDefault())
        return df2.format(date)
    }

    fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return dateToSeconds(calendar.time)
    }

    fun getNextDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return dateToSeconds(calendar.time)
    }

    fun getEndOfWeek(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY) // Thiết lập là chủ nhật
        calendar.add(Calendar.WEEK_OF_YEAR, 1) // Chuyển đến tuần tiếp theo
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return dateToSeconds(calendar.time)
    }

    fun getStartOfWeek(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek) // Lấy ngày đầu tiên của tuần
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return dateToSeconds(calendar.time)
    }

    fun getTimeCurrent(): Long {
        val currentTime = Date()
        return dateToSeconds(currentTime)
    }

    private fun dateToSeconds(date: Date): Long {
        return date.time / 1000
    }
}
