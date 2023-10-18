package vn.tutorme.mobile.data.source.remote.network

import java.lang.Boolean

object BuildConfig {
    val DEBUG = Boolean.parseBoolean("true")

    val BASE_DOMAIN: String = "http://192.168.0.106:8085/"
    val BASE_DOMAIN_HOME: String = "http://192.168.65.102:8085/"
    val BASE_DOMAIN_COMPANY: String = "http://192.168.4.218:8085/"
}
