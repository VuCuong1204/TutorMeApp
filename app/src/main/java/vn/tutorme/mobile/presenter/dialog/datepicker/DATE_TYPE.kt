package vn.tutorme.mobile.presenter.dialog.datepicker

import android.util.Log

enum class DATE_TYPE(val type: Int?) {
    NORMAL(0),
    PASS(1),
    FEATURE(2);

    companion object {
        fun valueOfName(value: Int?): DATE_TYPE? {
            val item = values().find {
                it.type == value
            }

            if (item == null) {
                Log.e("DATE_TYPE", "can not find any DATE_TYPE for name: $value")
            }

            return item
        }
    }
}
