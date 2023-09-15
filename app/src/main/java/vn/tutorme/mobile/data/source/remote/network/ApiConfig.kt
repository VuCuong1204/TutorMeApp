package vn.tutorme.mobile.data.source.remote.network

object ApiConfig {
    val BASE_URL = BuildConfig.BASE_DOMAIN

    object HeaderName {
        const val CONTENT_TYPE = "Content-Type"
        const val AUTHORIZATION = "Authorization"
        const val ACCEPT_LANGUAGE = "Accept-Language"
    }

    object HeaderValue {
        const val APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
        const val APPLICATION_JSON = "application/json"
        const val APPLICATION_OCTET_STREAM = "application/octet-stream"
        const val X_OS_VALUE = "ANDROID"
    }
}
