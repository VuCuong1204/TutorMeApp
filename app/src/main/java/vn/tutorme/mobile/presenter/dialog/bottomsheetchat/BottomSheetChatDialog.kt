package vn.tutorme.mobile.presenter.dialog.bottomsheetchat

import androidx.core.widget.addTextChangedListener
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.BottomSheetChatDialogBinding

class BottomSheetChatDialog : TutorMeDialog<BottomSheetChatDialogBinding>(R.layout.bottom_sheet_chat_dialog) {

    companion object {
        const val CHAT_INFO_CHILD_KEY = "ChatRoom"
    }

    private val bottomSheetChatAdapter by lazy { BottomSheetChatAdapter() }

    private lateinit var myRef: DatabaseReference
    private lateinit var postListener: ValueEventListener
    var lessonId: Int? = null
    var dataList: MutableList<ChatRoomInfoDisplay> = mutableListOf()

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = true
    }

    override fun getBackgroundId(): Int = R.id.llBottomSheetChatRoot

    override fun onDestroyView() {
        myRef.removeEventListener(postListener)
        super.onDestroyView()
    }

    override fun onInitView() {
        super.onInitView()
        addMessageListenerEvent()
        binding.pbBottomSheetChatLoading.show()
        binding.tvBottomSheetChatCount.text = String.format(getAppString(R.string.count_commenter), 0)

        binding.ivBottomSheetChatClose.setOnSafeClick {
            dismiss()
        }

        binding.edtBottomSheetChatContent.addTextChangedListener {
            if (it?.isEmpty() == true) {
                binding.ivBottomSheetChatSend.setImageResource(R.drawable.ic_send_disable)
                binding.ivBottomSheetChatSend.isEnabled = false
            } else {
                binding.ivBottomSheetChatSend.setImageResource(R.drawable.ic_send)
                binding.ivBottomSheetChatSend.isEnabled = true
            }
        }

        binding.ivBottomSheetChatSend.setOnSafeClick {
            binding.ivBottomSheetChatSend.setImageResource(R.drawable.ic_send_disable)
            binding.ivBottomSheetChatSend.isEnabled = false
            val chatRoomInfoDisplay = ChatRoomInfoDisplay(
                id = AppPreferences.userInfo?.userId,
                userName = AppPreferences.userInfo?.fullName,
                avatar = AppPreferences.userInfo?.avatar,
                content = binding.edtBottomSheetChatContent.text.toString().trim(),
                gender = AppPreferences.userInfo?.gender,
                time = System.currentTimeMillis().div(1000)
            )

            binding.edtBottomSheetChatContent.setText("")
            sendMessageListenerEvent(chatRoomInfoDisplay)
        }

        binding.cvBottomSheetChatRoot.apply {
            setBaseLayoutManager(layout = LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL, isReverse = true)
            setBaseAdapter(bottomSheetChatAdapter)
        }
    }

    private fun addMessageListenerEvent() {
        myRef = FirebaseDatabase.getInstance().getReference(CHAT_INFO_CHILD_KEY).child(lessonId.toString())
        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                dataSnapshot.children.forEach {
                    val chatDisplay = it.getValue(ChatRoomInfoDisplay::class.java)
                    chatDisplay?.let { chat -> dataList.add(0, chat) }
                }

                if (dataList.isNotEmpty()) {
                    binding.cvBottomSheetChatRoot.submitList(dataList)
                    binding.cvBottomSheetChatRoot.smoothScrollToPosition(0)
                    binding.tvBottomSheetChatCount.text = String.format(getAppString(R.string.count_commenter), dataList.size)
                } else {
                    binding.cvBottomSheetChatRoot.submitList(dataList)
                }

                binding.pbBottomSheetChatLoading.gone()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                if (dataList.isEmpty()) {
                    binding.cvBottomSheetChatRoot.submitList(dataList)
                }
                binding.pbBottomSheetChatLoading.gone()
            }
        }
        myRef.addValueEventListener(postListener)
    }

    private fun sendMessageListenerEvent(value: ChatRoomInfoDisplay) {
        val chatId = myRef.push().key

        if (chatId != null) {
            myRef.child(chatId).setValue(value)
                .addOnFailureListener {
                    showMessage(getAppString(R.string.send_message_error))
                }
        }
    }
}
