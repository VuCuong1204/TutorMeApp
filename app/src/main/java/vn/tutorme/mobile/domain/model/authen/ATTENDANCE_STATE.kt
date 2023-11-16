package vn.tutorme.mobile.domain.model.authen

enum class ATTENDANCE_STATE(val value: Int) {
    NO_ROLL_CALLED_STATE(0),
    ROLL_CALLED_STATE(1);

    companion object {
        fun valuesOfName(state: Int?): ATTENDANCE_STATE {
            val item = ATTENDANCE_STATE.values().find {
                it.value == state
            }

            return item ?: NO_ROLL_CALLED_STATE
        }
    }
}
