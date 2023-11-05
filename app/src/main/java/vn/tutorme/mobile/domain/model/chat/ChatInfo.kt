package vn.tutorme.mobile.domain.model.chat

data class ChatInfo(
    var id: Int? = null,
    var sendId: String? = null,
    var receiverId: String? = null,
    var content: String? = null,
    var url: String? = null
)

data class SingleRoomInfo(
    var sendId: String? = null,
    var receiverId: String? = null,
    var avatarSend: String? = null,
    var avatarReceiver: String? = null,
    var nameSend: String? = null,
    var nameReceiver: String? = null
)
