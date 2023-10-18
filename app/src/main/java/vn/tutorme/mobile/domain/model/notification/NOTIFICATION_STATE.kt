package vn.tutorme.mobile.domain.model.notification

enum class NOTIFICATION_STATE(val value: Int) {
    UNREAD_STATE(0),
    READ_STATE(1);

    companion object {
        fun valueOfName(type: Int): NOTIFICATION_STATE {
            val item = NOTIFICATION_STATE.values().find {
                it.value == type
            }
            return item ?: UNREAD_STATE
        }
    }
}
