package vn.tutorme.mobile.base.common.exception

class ApiException : BaseException {
    companion object {
        /**
         * code in app
         */
        const val NETWORK_ERROR = 20002
        const val TIME_OUT_ERROR = 20003
        const val SERVER_ERROR_CODE_UNDEFINE = 20004
        const val RESPONSE_BODY_ERROR = 20005
        const val CREATE_INSTANCE_SERVICE_ERROR = 20006
        const val CONVERT_JSON_FROM_RESPONSE_ERROR = 20008
        const val ERROR_DETECT_FACE = 20009
    }

    constructor(code: Int) : super(code)

    constructor(message: String?) : super(message)

    constructor(code: Int, message: String?) : super(code, message)

    constructor(code: Int, message: String?, throwable: Throwable?) : super(code, message, throwable)

    constructor(code: Int, message: String?, throwable: Throwable?, payload: Any?) : super(code, message, throwable, payload)
}
