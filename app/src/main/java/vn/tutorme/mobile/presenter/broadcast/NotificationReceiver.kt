package vn.tutorme.mobile.presenter.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "ACTION_NOTIFICATION_CLICK") {
            Log.d("TAG", "onReceive: ")
        }
    }
}
