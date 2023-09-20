package vn.tutorme.mobile.base.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
