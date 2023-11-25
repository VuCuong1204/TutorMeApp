package vn.tutorme.mobile.domain.model.profile

import java.util.Calendar

enum class DAY_TYPE(val value: Int) {
    MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),
    WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),
    FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY),
    SUNDAY(Calendar.SUNDAY);

    companion object {
        fun valueOfName(value: Int?): DAY_TYPE {
            return values().find {
                it.value == value
            } ?: MONDAY
        }
    }
}
