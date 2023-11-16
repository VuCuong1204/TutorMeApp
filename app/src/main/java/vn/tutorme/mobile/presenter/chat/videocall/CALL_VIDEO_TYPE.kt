package vn.tutorme.mobile.presenter.chat.videocall

enum class CALL_VIDEO_TYPE(val value: Int) {
    INCOMING_TYPE(0),
    OUT_GOING_CALL_TYPE(1);

    companion object {
        fun valuesOfName(type: Int?): CALL_VIDEO_TYPE {
            val item = CALL_VIDEO_TYPE.values().find {
                it.value == type
            }

            return item ?: OUT_GOING_CALL_TYPE
        }
    }
}
