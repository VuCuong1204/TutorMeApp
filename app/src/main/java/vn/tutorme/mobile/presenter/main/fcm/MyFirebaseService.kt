package vn.tutorme.mobile.presenter.main.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.InsertNotificationState
import vn.tutorme.mobile.base.common.UpdateNotificationListState
import vn.tutorme.mobile.base.common.eventbus.EventBusManager
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_STATE
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_TYPE
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.domain.model.notification.RefInfo
import vn.tutorme.mobile.presenter.main.MainActivity
import vn.tutorme.mobile.utils.TimeUtils

class MyFirebaseService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "TUTOR ME1"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.notification?.title != null && message.notification?.body != null) {
            sendNotification(
                message.notification?.title!!,
                message.notification?.body!!,
                message.data["lessonId"],
                message.data["classId"],
                NOTIFICATION_TYPE.valueOfName(message.data["notificationType"]?.toInt())
            )
            Log.d("TAG", "Message Notification Body: " + message.notification?.title!! + message.notification!!.body + message.data["lessonId"] + message.data["classId"])
            EventBusManager.instance?.postPending(UpdateNotificationListState())
            EventBusManager.instance?.postPending(InsertNotificationState(
                NotificationInfo(
                    title = message.notification?.title,
                    content = message.notification?.body,
                    notifyState = NOTIFICATION_STATE.UNREAD_STATE,
                    notifyType = NOTIFICATION_TYPE.valueOfName(message.data["notificationType"]?.toInt()),
                    timeSend = TimeUtils.getTimeCurrent(),
                    userId = AppPreferences.userInfo?.userId,
                    refInfo = RefInfo(
                        lessonId = message.data["lessonId"]?.toInt(),
                        classId = message.data["classId"]
                    )
                )
            ))
        }
    }

    private fun sendNotification(
        messageTitle: String,
        messageBody: String,
        lessonId: String?,
        classId: String?,
        notificationType: NOTIFICATION_TYPE
    ) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("title", messageTitle)
        intent.putExtra("body", messageTitle)
        intent.putExtra("lessonId", lessonId)
        intent.putExtra("classId", classId)
        intent.putExtra("notificationType", notificationType)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = Uri.parse("android.resources://" + packageName + "/" + R.raw.notification_tutor_me)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_1)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(VISIBILITY_PUBLIC)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(defaultSoundUri, audioAttributes)
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}
