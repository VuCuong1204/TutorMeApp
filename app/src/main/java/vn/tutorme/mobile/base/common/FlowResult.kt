package vn.tutorme.mobile.base.common

class FlowResult<DATA> : BaseUiState() {
    var data: DATA? = null

    companion object {
        @JvmStatic
        fun <T> newInstance(): FlowResult<T> {
            return FlowResult()
        }
    }

    fun initial() = apply {
        setUiState(UiState.UI_STATE.INITIAL, "initial ")
        return this
    }

    fun loading(message: String? = null) = apply {
        setUiState(UiState.UI_STATE.LOADING, message ?: "loading !!")
        return this
    }

    fun failure(throwable: Throwable, data: DATA? = null) = apply {
        setUiState(UiState.UI_STATE.FAILURE, throwable.message ?: "error !!")
        this.data = data
        this.throwable = throwable
        return this
    }

    fun success(data: DATA? = null) = apply {
        setUiState(UiState.UI_STATE.SUCCESS)
        this.data = data
        return this
    }

    fun reset() = apply {
        initial()
        this.data = null
    }
}
