package vn.tutorme.mobile.domain.model.notification

enum class NOTIFICATION_TYPE(val value: Int) {
    PREPARE_STUDY_TYPE(0),
    APPROVE_TYPE(1),
    SYSTEM_TYPE(2);

    companion object {
        fun valueOfName(type: Int): NOTIFICATION_TYPE {
            val item = NOTIFICATION_TYPE.values().find {
                it.value == type
            }
            return item ?: SYSTEM_TYPE
        }
    }
}
