package vn.tutorme.mobile.base.common

import android.net.Uri
import vn.tutorme.mobile.base.common.eventbus.IEvent

class NotifyDetailResultEvent(val notifyId: Int) : IEvent
class CountNotifyEvent() : IEvent
class GetUriFaceDetection(val uri: Uri) : IEvent
