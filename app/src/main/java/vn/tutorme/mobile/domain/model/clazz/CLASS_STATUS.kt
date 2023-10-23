package vn.tutorme.mobile.domain.model.clazz

enum class CLASS_STATUS(val value: Int) {
    EMPTY_CLASS_STATUS(0),
    RECEIVED_STATUS(1),
    CANCEL_STATUS(2),
    OUT_OF_DATE_STATUS(3);

    companion object {
        fun valuesOfName(status: Int): CLASS_STATUS? {
            val item = CLASS_STATUS.values().find {
                it.value == status
            }

            return item
        }
    }
}
