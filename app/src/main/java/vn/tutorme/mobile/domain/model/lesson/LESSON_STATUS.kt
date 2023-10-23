package vn.tutorme.mobile.domain.model.lesson

enum class LESSON_STATUS(val value: Int) {
    UPCOMING_STATUS(0),
    HAPPENING_STATUS(1),
    CANCEL_STATUS(2),
    TOOK_PLACE_STATUS(3);

    companion object {
        fun valuesOfName(value: Int): LESSON_STATUS? {
            val item = LESSON_STATUS.values().find {
                it.value == value
            }

            return item
        }
    }
}
