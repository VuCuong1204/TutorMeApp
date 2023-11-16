package vn.tutorme.mobile.presenter.dialog.bottomsheetchat

import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE
import vn.tutorme.mobile.utils.TimeUtils

data class ChatRoomInfoDisplay(
    var id: String? = null,
    var userName: String? = null,
    var avatar: String? = null,
    var content: String? = null,
    var gender: GENDER_TYPE? = null,
    var time: Long? = null
) {
    fun getTimeHour(): String {
        return TimeUtils.calculatorTimeAgo(time)
    }
}

fun mockDataChatRoomInfoDisplay(size: Int = 20): List<ChatRoomInfoDisplay> {
    val list = mutableListOf<ChatRoomInfoDisplay>()
    repeat(size) {
        list.add(ChatRoomInfoDisplay(
            id = "$it",
            userName = "Vũ Cường",
            avatar = "",
            content = "Hello mọi người , mấy giờ học vậy",
            gender = GENDER_TYPE.MALE_TYPE,
            time = 1698486996
        ))
    }

    return list
}
