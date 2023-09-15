package vn.tutorme.mobile.base.common.eventbus

import org.greenrobot.eventbus.EventBus

class EventBusManager {
    companion object {
        var instance: EventBusManager? = null
            get() {
                if (field == null) {
                    synchronized(EventBusManager::class.java) {
                        field = EventBusManager()
                    }
                }
                return field
            }
    }

    fun register(eventHandler: IEventHandler) {
        EventBus.getDefault().register(eventHandler)
    }

    fun unregister(eventHandler: IEventHandler) {
        EventBus.getDefault().unregister(eventHandler)
    }

    fun post(event: IEvent) {
        EventBus.getDefault().post(event)
    }

    fun postPending(event: IEvent) {
        EventBus.getDefault().postSticky(event)
    }

    fun removeSticky(event: IEvent) {
        EventBus.getDefault().removeStickyEvent(event)
    }
}
