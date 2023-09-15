package vn.tutorme.mobile.base.common.exception

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString

object HandleExceptionImpl : IHandleException {
    override fun getMessage(exception: BaseException): String? {
        return when (exception) {
            is ApiException -> getMessageByApiException(exception)
            else -> null
        }
    }

    private fun getMessageByApiException(e: ApiException): String {
        return when (e.code) {
            ApiException.NETWORK_ERROR -> getAppString(R.string.no_network)
            ApiException.TIME_OUT_ERROR -> getAppString(R.string.server_time_out)
            ApiException.CONVERT_JSON_FROM_RESPONSE_ERROR -> getAppString(R.string.convert_response_error)
            ApiException.RESPONSE_BODY_ERROR -> getAppString(R.string.response_error)
            else -> {
                e.message.toString()
            }
        }
    }
}

interface IHandleException {
    fun getMessage(exception: BaseException): String?
}
