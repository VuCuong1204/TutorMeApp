package vn.tutorme.mobile.base.application

import android.app.Application

private var application: Application? = null

fun setApplication(context: Application) {
    application = context
}

fun getApplication() = application ?:throw java.lang.RuntimeException("Application context mustn't null")
