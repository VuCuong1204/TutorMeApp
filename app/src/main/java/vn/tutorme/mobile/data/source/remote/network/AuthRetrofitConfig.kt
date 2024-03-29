package vn.tutorme.mobile.data.source.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthRetrofitConfig : BaseRetrofitConfig() {

    override fun getUrl(): String = ApiConfig.BASE_URL

    override fun getInterceptorList(): Array<Interceptor> {
        return arrayOf(AuthInterceptor())
    }

    class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = builderChan(chain)
            return chain.proceed(requestBuilder.build())
        }

        private fun builderChan(chain: Interceptor.Chain): Request.Builder {
            val original = chain.request()
            val builder = original.newBuilder()
            builder.addHeader(ApiConfig.HeaderName.CONTENT_TYPE, ApiConfig.HeaderValue.APPLICATION_JSON)
//            AppPreferences.accountInfo?.accessToken?.let {
//                builder.addHeader(ApiConfig.HeaderName.AUTHORIZATION, "Bearer $it")
//            }

            return builder
        }
    }
}
