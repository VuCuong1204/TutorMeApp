package vn.tutorme.mobile.presenter.dialog.bottomsheetchat

import androidx.databinding.ViewDataBinding
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.loadUser
import vn.tutorme.mobile.databinding.BottomSheetChatItemBinding
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE

class BottomSheetChatAdapter : TutorMeAdapter() {

    override fun getLayoutResource(viewType: Int): Int = R.layout.bottom_sheet_chat_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return BottomSheetChatVH(binding as BottomSheetChatItemBinding)
    }

    inner class BottomSheetChatVH(private val binding: BottomSheetChatItemBinding) : BaseVH<ChatRoomInfoDisplay>(binding) {
        override fun onBind(data: ChatRoomInfoDisplay) {
            super.onBind(data)

            with(binding) {
                ivBottomSheetChatAvatar.loadUser(
                    url = data.avatar,
                    placeHolder = if (data.gender == GENDER_TYPE.MALE_TYPE)
                        getAppDrawable(R.drawable.bg_gender_male) else
                        getAppDrawable(R.drawable.bg_gender_female)
                )

                tvBottomSheetChatTitle.text = data.userName
                tvBottomSheetChatContent.text = data.content
                tvBottomSheetChatTime.text = data.getTimeHour()
            }
        }
    }
}
