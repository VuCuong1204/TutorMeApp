package vn.tutorme.mobile.data.repo.notification

import vn.tutorme.mobile.data.repo.convert.NotificationInfoDTOConvertToNotificationInfo
import vn.tutorme.mobile.data.source.remote.base.IRepo
import vn.tutorme.mobile.data.source.remote.base.invokeApi
import vn.tutorme.mobile.data.source.remote.base.invokeAuthService
import vn.tutorme.mobile.data.source.remote.base.invokeNotificationService
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

    override fun getCountNotificationUnRead(userId: String): Int {
        val service = invokeAuthService(INotificationService::class.java)

        return service.getCountNotificationUnRead(userId).invokeApi { _, body ->
            if (body.data != null) {
                body.data!!
            } else 0
        }
    }

    override fun insertNotification(
        title: String,
        content: String,
        notifyState: Int,
        notifyType: Int,
        timeSend: Long,
        userId: String,
        lessonId: Int,
        classId: String,
    ): Boolean {
        val service = invokeAuthService((INotificationService::class.java))
        return service.insertNotification(
            title,
            content,
            notifyState,
            notifyType,
            timeSend,
            userId,
            lessonId,
            classId
        ).invokeApi { _, _ ->
            true
        }
    }

    override fun sendNotificationBeginLesson(
        tokens: String,
        lessonId: String,
        classId: String,
        title: String,
        body: String,
        notificationId: String
    ): Boolean {
        val service = invokeNotificationService(INotificationService::class.java)
        return service.sendNotificationBeginLesson(tokens, lessonId, classId, title, body, notificationId).invokeApi { _, _ ->
            true
        }
    }
}
