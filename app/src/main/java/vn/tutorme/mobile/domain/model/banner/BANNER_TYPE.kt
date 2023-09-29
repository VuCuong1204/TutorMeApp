package vn.tutorme.mobile.domain.model.banner

import android.util.Log

enum class BANNER_TYPE(val value: Int) {
    JOB_TYPE(0),
    EVENT_TYPE(1),
    COURSE_TYPE(2);

    companion object {
        fun valuesOfName(value: Int?): BANNER_TYPE? {
            val item = BANNER_TYPE.values().find {
                it.value == value
            }

            if (item == null) {
                Log.d("BANNER_TYPE", "Can't find BANNER_TYPE with value: $value")
            }

            return item
        }
    }
}
