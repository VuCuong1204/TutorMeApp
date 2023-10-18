package vn.tutorme.mobile.presenter.notification

import vn.tutorme.mobile.domain.model.notification.NotificationInfo

interface INotificationListener {
    fun onReadClick(item: NotificationInfo)
    fun onDeleteClick(id: String)
}
