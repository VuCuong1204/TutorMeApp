package vn.tutorme.mobile.data.source.remote.network

import android.util.Log
import retrofit2.Retrofit
import vn.tutorme.mobile.base.common.exception.ApiException
import java.util.concurrent.ConcurrentHashMap

object RetrofitFactory {
    private val TAG = RetrofitFactory::class.java.simpleName
    private const val AUTH = "AUTH"
    private const val LOCATION = "LOCATION"
    private const val STRINGEE = "STRINGEE"

    private val builderMap = ConcurrentHashMap<String, RetrofitBuilderInfo>()

    fun <T> createAuthService(service: Class<T>): T {
        synchronized(RetrofitBuilderInfo::class.java) {
            var builderInfo = builderMap[AUTH]
            if (builderInfo == null) {
                builderInfo = RetrofitBuilderInfo()
                builderInfo.builder = AuthRetrofitConfig().getRetrofitBuilder()
                builderMap[AUTH] = builderInfo
                Log.d(TAG, "Create new domain retrofit builder for $AUTH")
            }
            Log.e(TAG, "Reuse domain retrofit builder for $AUTH")
            val serviceApi = builderInfo.builder?.build()?.create(service)

            return serviceApi ?: throw ApiException(ApiException.CREATE_INSTANCE_SERVICE_ERROR)
        }
    }

    fun <T> createLocationService(service: Class<T>): T {
        synchronized(RetrofitBuilderInfo::class.java) {
            var builderInfo = builderMap[LOCATION]
            if (builderInfo == null) {
                builderInfo = RetrofitBuilderInfo()
                builderInfo.builder = LocationRetrofitConfig().getRetrofitBuilder()
                builderMap[LOCATION] = builderInfo
                Log.d(TAG, "Create new domain retrofit builder for $LOCATION")
            }
            Log.e(TAG, "Reuse domain retrofit builder for $LOCATION")
            val serviceApi = builderInfo.builder?.build()?.create(service)

            return serviceApi ?: throw ApiException(ApiException.CREATE_INSTANCE_SERVICE_ERROR)
        }
    }

    fun <T> createStringeeService(service: Class<T>): T {
        synchronized(RetrofitBuilderInfo::class.java) {
            var builderInfo = builderMap[STRINGEE]
            if (builderInfo == null) {
                builderInfo = RetrofitBuilderInfo()
                builderInfo.builder = StringeeRetrofitConfig().getRetrofitBuilder()
                builderMap[STRINGEE] = builderInfo
                Log.d(TAG, "Create new domain retrofit builder for $STRINGEE")
            }
            Log.e(TAG, "Reuse domain retrofit builder for $STRINGEE")
            val serviceApi = builderInfo.builder?.build()?.create(service)

            return serviceApi ?: throw ApiException(ApiException.CREATE_INSTANCE_SERVICE_ERROR)
        }
    }

    class RetrofitBuilderInfo {
        var builder: Retrofit.Builder? = null
        var accessToken: String? = null

        fun valid(accessToken: String?): Boolean {
            return this.accessToken == accessToken
        }
    }
}
