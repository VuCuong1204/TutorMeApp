package vn.tutorme.mobile.base.common

import android.graphics.Bitmap
import vn.tutorme.mobile.base.common.eventbus.IEvent
import vn.tutorme.mobile.domain.model.notification.NotificationInfo
import vn.tutorme.mobile.presenter.chat.videocall.CALL_VIDEO_STATE

class NotifyDetailResultEvent(val notifyId: Int) : IEvent
class CountNotifyEvent() : IEvent
class GetImageDetect(val bitmap: Bitmap) : IEvent
class SendVideoCallState(val state: CALL_VIDEO_STATE) : IEvent
class InsertNotificationState(val notificationInfo: NotificationInfo) : IEvent
class UpdateNotificationListState() : IEvent
class NavigateLessonInfo(val lessonId: Int, val classId: String) : IEvent
