package vn.tutorme.mobile.data.source.remote.network

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import vn.tutorme.mobile.base.application.getApplication
import vn.tutorme.mobile.data.source.remote.socket.SSLSocketClient
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

abstract class BaseRetrofitConfig {
    abstract fun getUrl(): String
    abstract fun getInterceptorList(): Array<Interceptor>

    open fun getAuthenticator(): Authenticator = Authenticator.NONE

    fun getRetrofit(): Retrofit {
        return getRetrofitBuilder().build()
    }

    fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(getUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
//            .addConverterFactory(NullOnEmptyConverterFactory())
            .client(provideOkHttpClient())
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(ChuckerInterceptor.Builder(getApplication())
                .collector(ChuckerCollector(getApplication()))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(true)
                .build())
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        builder.addInterceptor(logging)

        val interceptorList = getInterceptorList()

        interceptorList.forEach {
            builder.addInterceptor(it)
        }

        val timeout = 30L

        builder.connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), object : X509TrustManager {
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            .hostnameVerifier { _, _ ->
                true
            }

        val authenticator = getAuthenticator()
        if (authenticator != Authenticator.NONE) {
            builder.authenticator(authenticator)
        }

        return builder.build()
    }

    private fun getGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
//            .registerTypeAdapter(java.util.Date::class.java, GsonUtcDateAdapter())
    }
}
