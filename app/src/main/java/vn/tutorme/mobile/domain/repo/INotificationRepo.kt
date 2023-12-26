package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.notification.NotificationInfo

interface INotificationRepo {
    fun getNotificationList(userId: String, page: Int?, size: Int?): List<NotificationInfo>
    fun deleteNotification(notificationId: Int): Boolean
    fun updateNotification(notificationId: Int): Boolean
    fun updateNotificationAll(userId: String): Boolean
    fun insertNotification(
        title: String,
        content: String,
        notifyState: Int,
        notifyType: Int,
        timeSend: Long,
        userId: String,
        lessonId: Int,
        classId: String
    ): Boolean

    fun getCountNotificationUnRead(userId: String): Int
    fun sendNotificationBeginLesson(
        tokens: String,
        lessonId: String,
        classId: String,
        title: String,
        body: String,
        notificationId: String
    ): Boolean
}
