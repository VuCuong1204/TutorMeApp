package vn.tutorme.mobile.domain.model.authen

import android.util.Log

enum class ROLE_TYPE(val value: Int) {
    STUDENT_TYPE(0),
    TEACHER_TYPE(1);

    companion object {
        fun valuesOfName(value: Int) {
            val item = ROLE_TYPE.values().find {
                it.value == value
            }

            if (item == null) {
                Log.d("ROLE_TYPE", "Can't find ROLE_TYPE with value: $value")
            }
        }
    }
}
