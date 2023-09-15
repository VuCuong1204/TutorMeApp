package vn.tutorme.mobile.data.source.remote.base

import com.google.gson.Gson
import retrofit2.Response
import vn.tutorme.mobile.base.common.exception.ApiException

object ExceptionHelper {

    private const val HTTP_CODE_SUCCESSFUL_OK = 200

    fun throwException(response: Response<*>): ApiException {
        return when (response.code()) {
            HTTP_CODE_SUCCESSFUL_OK -> getAPIExceptionWhenHTTPCodeSuccessful(response)
            else -> getAPIExceptionWhenHTTPCodeUnsuccessful(response)
        }
    }

    private fun getAPIExceptionWhenHTTPCodeSuccessful(response: Response<*>): ApiException {
        val body = response.body()
        if (body != null) {
            val apiResponse = body as BaseApiResponse
            val msg = apiResponse.msg
            val code = apiResponse.code ?: ApiException.SERVER_ERROR_CODE_UNDEFINE
            return ApiException(code, msg)
        }

        return ApiException(ApiException.RESPONSE_BODY_ERROR)
    }

    private fun getAPIExceptionWhenHTTPCodeUnsuccessful(response: Response<*>): ApiException {
        val errorBody = response.errorBody()
        if (errorBody != null) {
            val errorResponse = Gson().fromJson(errorBody.charStream(), BaseApiResponse::class.java)
            var msg = errorResponse.msg
            if (msg == null) {
                msg = response.message()
            }
            val code = errorResponse.code ?: response.code()
            return ApiException(code, msg)
        }
        return ApiException(response.code(), response.message())
    }
}
