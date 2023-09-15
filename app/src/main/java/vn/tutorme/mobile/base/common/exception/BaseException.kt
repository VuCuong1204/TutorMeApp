package vn.tutorme.mobile.base.common.exception

open class BaseException : Exception {
    companion object {
        const val UNKNOWN_ERROR = 10001
        const val DEV_DEBUG_LOCAL_ERROR = 10002
    }

    var code: Int
    var payload: Any? = null

    constructor() : super() {
        code = UNKNOWN_ERROR
    }

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(message: String?) : super(message) {
        this.code = UNKNOWN_ERROR
    }

    constructor(code: Int, message: String?) : super(message) {
        this.code = code
    }

    constructor(code : Int,message: String?,throwable: Throwable?) : super(message,throwable){
        this.code = code
    }

    constructor(code: Int, message: String?, throwable: Throwable?, payload: Any?) : super(message, throwable) {
        this.code = code
        this.payload = payload
    }
}
