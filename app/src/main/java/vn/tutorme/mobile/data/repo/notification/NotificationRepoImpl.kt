package vn.tutorme.mobile.data.repo.notification

import vn.tutorme.mobile.data.repo.convert.NotificationInfoDTOConvertToNotificationInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeApi
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.service.INotificationService
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.domain.repo.INotificationRepo
import javax.inject.Inject

class NotificationRepoImpl @Inject constructor() : INotificationRepo, IRepo {
    override fun getNotificationList(userId: String, page: Int?, size: Int?): List<NotificationInfo> {
        val service = invokeAuthService(INotificationService::class.java)

        return service.getNotificationList(userId, page, size).invokeApi { _, body ->
            NotificationInfoDTOConvertToNotificationInfo().convert(body.data!!)
        }
    }

    override fun deleteNotification(notificationId: Int): Boolean {
        val service = invokeAuthService(INotificationService::class.java)

        return service.deleteNotification(notificationId).invokeApi { _, _ ->
            true
        }
    }

    override fun updateNotification(notificationId: Int): Boolean {
        val service = invokeAuthService(INotificationService::class.java)

        return service.updateNotification(notificationId).invokeApi { _, _ ->
            true
        }
    }

    override fun updateNotificationAll(userId: String): Boolean {
        val service = invokeAuthService(INotificationService::class.java)

        return service.updateNotificationAll(userId).invokeApi { _, _ ->
            true
        }
    }
}
