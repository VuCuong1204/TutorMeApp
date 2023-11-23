package vn.tutorme.mobile.data.source.remote.network

import java.lang.Boolean
import kotlin.String

object BuildConfig {
    val DEBUG = Boolean.parseBoolean("true")

    val BASE_DOMAIN: String = "http://192.168.0.108:8085/"
    val LOCATION_DOMAIN: String = "https://social.fqa.vn/"
    val STRINGEE_DOMAIN: String = "https://minhvt18147.000webhostapp.com/php/"
    val TENSORFLOW_DOMAIN: String = "http://192.168.0.105:8080/"
    val BASE_DOMAIN_HOME: String = "http://192.168.65.102:8085/"
    val BASE_DOMAIN_COMPANY: String = "http://192.168.4.218:8085/"
}
