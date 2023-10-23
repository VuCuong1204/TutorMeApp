package vn.tutorme.mobile.domain.model.lesson

enum class LESSON_TYPE(val value: Int) {
    NOT_YET_RATE_TYPE(0),
    YET_RATE_TYPE(1);

    companion object {
        fun valueOfName(value: Int?): LESSON_TYPE {
            val item = LESSON_TYPE.values().find {
                it.value == value
            }

            return item ?: NOT_YET_RATE_TYPE
        }
    }
}
