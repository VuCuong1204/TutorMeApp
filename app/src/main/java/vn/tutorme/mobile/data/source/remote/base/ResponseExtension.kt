package vn.tutorme.mobile.data.source.remote.base

import okhttp3.Headers
import retrofit2.Call
import vn.tutorme.mobile.base.common.exception.ApiException
import vn.tutorme.mobile.data.source.remote.network.RetrofitFactory
import java.io.EOFException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun <RESPONSE : IApiResponse, RETURN_VALUE> Call<RESPONSE>.invokeApi(
    block: (Headers, RESPONSE) -> RETURN_VALUE
): RETURN_VALUE {
    try {
        val response = this.execute()
        if (response.isSuccessful) {
            val body: RESPONSE? = response.body()
            if (body != null) {
                if (body.isSuccessful()) {
                    return block(response.headers(), body)
                }
            }
        }
        throw ExceptionHelper.throwException(response)
    } catch (e: Exception) {
        when (e) {
            is UnknownHostException -> throw ApiException(ApiException.NETWORK_ERROR)
            is SocketTimeoutException -> throw ApiException(ApiException.TIME_OUT_ERROR)
            is EOFException -> throw ApiException(ApiException.CONVERT_JSON_FROM_RESPONSE_ERROR)
            else -> throw e
        }
    }
}

fun <T : IApiService> IRepo.invokeAuthService(service: Class<T>): T {
    return RetrofitFactory.createAuthService(service)
}

fun <T : IApiService> IRepo.invokeLocationService(service: Class<T>): T {
    return RetrofitFactory.createLocationService(service)
}

fun <T : IApiService> IRepo.invokeStringeeService(service: Class<T>): T {
    return RetrofitFactory.createStringeeService(service)
}

fun <T : IApiService> IRepo.invokeTenserFlowService(service: Class<T>): T {
    return RetrofitFactory.createTensorflowService(service)
}

fun <T : IApiService> IRepo.invokeNotificationService(service: Class<T>): T {
    return RetrofitFactory.createNotificationService(service)
}
