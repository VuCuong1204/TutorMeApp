package vn.tutorme.mobile.domain.model.authen

import android.util.Log

enum class GENDER_TYPE(val value: Int) {
    MALE_TYPE(1),
    FEMALE_TYPE(2),
    OTHER(3);

    companion object {
        fun valuesOfName(value: Int) {
            val item = GENDER_TYPE.values().find {
                it.value == value
            }

            if (item == null) {
                Log.d("GENDER_TYPE", "Can't find GENDER_TYPE with value: $value")
            }
        }
    }
}
