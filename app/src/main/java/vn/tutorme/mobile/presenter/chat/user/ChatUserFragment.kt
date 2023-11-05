package vn.tutorme.mobile.presenter.chat.user

import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ChatUserFragmentBinding
import vn.tutorme.mobile.domain.model.chat.SingleRoomInfo

@AndroidEntryPoint
class ChatUserFragment : TutorMeFragment<ChatUserFragmentBinding>(R.layout.chat_user_fragment) {

    companion object {
        const val LIST_CHAT_KEY = "ListChatSingle"
    }

    private val chatUserAdapter by lazy { ChatUserAdapter() }
    private var dataList = mutableListOf<SingleRoomInfo>()

    override fun onInitView() {
        super.onInitView()

        binding.ivChatUserBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.cvChatUserRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(chatUserAdapter)
        }

        addListenerFireBase()
        addListener()
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    private fun addListenerFireBase() {
        if (AppPreferences.userInfo?.userId != null) {
            FirebaseDatabase.getInstance().getReference(LIST_CHAT_KEY).child(AppPreferences.userInfo?.userId!!)
                .get()
                .addOnSuccessListener {
                    it.children.forEach { dataSnapshot ->
                        val singleRoomInfo = dataSnapshot.getValue(SingleRoomInfo::class.java)
                        if (!dataList.contains(singleRoomInfo)) {
                            singleRoomInfo?.let { value -> dataList.add(value) }
                        }
                    }

                    binding.cvChatUserRoot.submitList(dataList)
                }
        }
    }

    private fun addListener() {
        chatUserAdapter.listener = object : ChatUserAdapter.IChatUserListener {
            override fun onItemClick(item: SingleRoomInfo) {
//                TODO("Not yet implemented")
            }
        }
    }

    private fun removeListener() {
        chatUserAdapter.listener = null
    }
}
