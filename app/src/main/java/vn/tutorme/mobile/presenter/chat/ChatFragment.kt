package vn.tutorme.mobile.presenter.chat

import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.loadUser
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ChatFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.domain.model.chat.ChatInfo
import vn.tutorme.mobile.domain.model.chat.SingleRoomInfo
import vn.tutorme.mobile.presenter.chat.videocall.VideoCallFragment

class ChatFragment : TutorMeFragment<ChatFragmentBinding>(R.layout.chat_fragment) {

    companion object {
        const val USER_ID_KEY = "USER_ID_KEY"
        const val USER_NAME_KEY = "USER_NAME_KEY"
        const val USER_AVATAR_KEY = "USER_AVATAR_KEY"
        const val ROOM_CHAT_KEY = "ChatSingle"
        const val LIST_CHAT_KEY = "ListChatSingle"
    }

    private var userReceiverId: String? = null
    private var userName: String? = null
    private var userAvatar: String? = null
    private lateinit var myRef: DatabaseReference
    private lateinit var postListener: ValueEventListener
    private var dataList: MutableList<ChatInfo> = mutableListOf()

    private val chatAdapter by lazy { ChatAdapter() }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        userReceiverId = arguments?.getString(USER_ID_KEY)
        userName = arguments?.getString(USER_NAME_KEY)
        userAvatar = arguments?.getString(USER_AVATAR_KEY)
    }

    override fun onInitView() {
        super.onInitView()

        binding.ivChatBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.edtChatContent.addTextChangedListener {
            if (it?.isEmpty() == true) {
                binding.ivChatSend.setImageResource(R.drawable.ic_send_disable)
                binding.ivChatSend.isEnabled = false
            } else {
                binding.ivChatSend.setImageResource(R.drawable.ic_send)
                binding.ivChatSend.isEnabled = true
            }
        }

        binding.ivHomeAvatar.loadUser(userAvatar)
        binding.tvChatName.text = userName ?: STRING_DEFAULT

        binding.ivChatSend.setOnSafeClick {
            binding.ivChatSend.setImageResource(R.drawable.ic_send_disable)
            binding.ivChatSend.isEnabled = false
            val chatInfo = ChatInfo(
                id = dataList.size,
                sendId = AppPreferences.userInfo?.userId,
                receiverId = userReceiverId,
                content = binding.edtChatContent.text.toString().trim(),
                url = AppPreferences.userInfo?.avatar
            )

            binding.edtChatContent.setText("")
            sendMessageListenerEvent(chatInfo)
        }

        binding.cvChatContent.apply {
            setBaseLayoutManager(layout = LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL, isReverse = true)
            setBaseAdapter(chatAdapter)
        }

        binding.ivChatCallVideo.setOnSafeClick {
            replaceFragment(fragment = VideoCallFragment(), bundleOf(
                VideoCallFragment.USER_FROM_KEY to AppPreferences.userInfo?.userId,
                VideoCallFragment.USER_TO_KEY to userReceiverId,
                VideoCallFragment.STATE_CALL_KEY to 1
            ))
        }

        addMessageListenerEvent()
    }

    override fun onDestroyView() {
        myRef.removeEventListener(postListener)
        super.onDestroyView()
    }

    private fun addMessageListenerEvent() {
        if (userReceiverId != null && AppPreferences.userInfo?.userId != null) {
            val receiverId = if (AppPreferences.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) AppPreferences.userInfo?.userId else userReceiverId
            val senderId = if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) AppPreferences.userInfo?.userId else userReceiverId
            myRef = FirebaseDatabase.getInstance().getReference(ROOM_CHAT_KEY).child(receiverId!!).child(senderId!!)
            postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataList.clear()
                    dataSnapshot.children.forEach {
                        val chatDisplay = it.getValue(ChatInfo::class.java)
                        chatDisplay?.let { chat -> dataList.add(0, chat) }
                    }

                    if (dataList.isNotEmpty()) {
                        binding.cvChatContent.submitList(dataList)
                        binding.cvChatContent.smoothScrollToPosition(0)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            myRef.addValueEventListener(postListener)
        }
    }

    private fun sendMessageListenerEvent(value: ChatInfo) {
        val chatId = myRef.push().key
        if (chatId != null) {
            myRef.child(chatId).setValue(value)
                .addOnFailureListener {
                    showMessage(getAppString(R.string.send_message_single_error))
                }
            if (userReceiverId != null && AppPreferences.userInfo?.userId != null) {
                FirebaseDatabase.getInstance().getReference(LIST_CHAT_KEY).child(AppPreferences.userInfo?.userId!!)
                    .get()
                    .addOnSuccessListener {

                        FirebaseDatabase.getInstance().getReference(LIST_CHAT_KEY).child(AppPreferences.userInfo?.userId!!).child(userReceiverId!!).removeValue()
                        FirebaseDatabase.getInstance().getReference(LIST_CHAT_KEY).child(userReceiverId!!).child(AppPreferences.userInfo?.userId!!).removeValue()

                        FirebaseDatabase.getInstance().getReference(LIST_CHAT_KEY).child(AppPreferences.userInfo?.userId!!).child(userReceiverId!!).setValue(
                            SingleRoomInfo(
                                sendId = AppPreferences.userInfo?.userId,
                                receiverId = userReceiverId,
                                avatarSend = AppPreferences.userInfo?.avatar,
                                avatarReceiver = userAvatar,
                                nameSend = AppPreferences.userInfo?.fullName,
                                nameReceiver = userName,
                            )
                        )

                        FirebaseDatabase.getInstance().getReference(LIST_CHAT_KEY).child(userReceiverId!!).child(AppPreferences.userInfo?.userId!!).setValue(
                            SingleRoomInfo(
                                sendId = userReceiverId,
                                receiverId = AppPreferences.userInfo?.userId,
                                avatarSend = userAvatar,
                                avatarReceiver = AppPreferences.userInfo?.avatar,
                                nameSend = userName,
                                nameReceiver = AppPreferences.userInfo?.fullName,
                            )
                        )
                    }
            }
        }
    }
}
