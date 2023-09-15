package vn.tutorme.mobile.base.common

abstract class BaseUiState {
    private var uiState: UiState = UiState()
    var throwable: Throwable? = null

    fun setUiState(state: UiState.UI_STATE, msg: String = "") {
        this.uiState = UiState().apply {
            this.state = state
            this.message = msg
        }
    }

    fun getUiState(): UiState.UI_STATE {
        return uiState.state
    }

    fun getMessage(): String {
        return uiState.message
    }
}

class UiState {
    var state: UI_STATE = UI_STATE.INITIAL
    var message: String = ""

    enum class UI_STATE {
        INITIAL, LOADING, SUCCESS, FAILURE
    }
}
