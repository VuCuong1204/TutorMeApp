package vn.tutorme.mobile

import dagger.hilt.android.HiltAndroidApp
import vn.tutorme.mobile.base.application.BaseApplication
import vn.tutorme.mobile.base.application.setApplication

@HiltAndroidApp
class TutorMeApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()

        AppPreferences.init(this)
        setApplication(this)
    }
}
