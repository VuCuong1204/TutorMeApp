package vn.tutorme.mobile.data.source.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TensorflowRetrofitConfig: BaseRetrofitConfig() {
    override fun getUrl(): String = ApiConfig.TENSORFLOW_URL

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
            return builder
        }
    }
}
