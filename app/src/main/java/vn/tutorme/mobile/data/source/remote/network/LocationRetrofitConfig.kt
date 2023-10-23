package vn.tutorme.mobile.data.source.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString

class LocationRetrofitConfig : BaseRetrofitConfig() {
    override fun getUrl(): String = ApiConfig.LOCATION_URL

    override fun getInterceptorList(): Array<Interceptor> {
        return arrayOf(LocationInterceptor())
    }

    class LocationInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = builderChan(chain)
            return chain.proceed(requestBuilder.build())
        }

        private fun builderChan(chain: Interceptor.Chain): Request.Builder {
            val original = chain.request()
            val builder = original.newBuilder()
            builder.addHeader(ApiConfig.HeaderName.CONTENT_TYPE, ApiConfig.HeaderValue.APPLICATION_JSON)
            builder.addHeader(ApiConfig.HeaderName.AUTHORIZATION, "Bearer ${getAppString(R.string.access_token)}")

            return builder
        }
    }
}
