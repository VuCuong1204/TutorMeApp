package vn.tutorme.mobile.domain.model.authen

enum class EVALUATE_STATE(val value: Int) {
    NO_EVALUATE_STATE(0),
    HAVE_EVALUATE_STATE(1);

    companion object {
        fun valuesOfName(state: Int?): EVALUATE_STATE {
            val item = EVALUATE_STATE.values().find {
                it.value == state
            }

            return item ?: NO_EVALUATE_STATE
        }
    }
}
