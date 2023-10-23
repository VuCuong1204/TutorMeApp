package vn.tutorme.mobile.domain.repo

import vn.tutorme.mobile.domain.model.notification.NotificationInfo

interface INotificationRepo {
    fun getNotificationList(userId: String, page: Int?, size: Int?): List<NotificationInfo>
    fun deleteNotification(notificationId: Int): Boolean
    fun updateNotification(notificationId: Int): Boolean
    fun updateNotificationAll(userId: String): Boolean
    fun getCountNotificationUnRead(userId: String): Int
}
