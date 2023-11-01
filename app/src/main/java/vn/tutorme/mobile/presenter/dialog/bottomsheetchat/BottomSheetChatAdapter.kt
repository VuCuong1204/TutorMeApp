package vn.tutorme.mobile.presenter.dialog.bottomsheetchat

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.loadUser
import vn.tutorme.mobile.base.extension.setCustomFont
import vn.tutorme.mobile.databinding.BottomSheetChatItemBinding
import vn.tutorme.mobile.domain.model.authen.GENDER_TYPE

class BottomSheetChatAdapter : TutorMeAdapter() {

    override fun getLayoutResource(viewType: Int): Int = R.layout.bottom_sheet_chat_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return BottomSheetChatVH(binding as BottomSheetChatItemBinding)
    }

    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return BottomSheetChatDiffCallBack(oldList, newList)
    }

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.chat_lesson_empty)

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
                if (data.id == AppPreferences.userInfo?.userId) {
                    binding.tvBottomSheetChatTitle.setTextColor(getAppColor(R.color.primary))
                    binding.tvBottomSheetChatTime.setCustomFont(fontPath = R.font.font_raleway_bold)
                } else {
                    binding.tvBottomSheetChatTitle.setTextColor(getAppColor(R.color.gray_30))
                    binding.tvBottomSheetChatTime.setCustomFont(fontPath = R.font.font_raleway_medium)
                }

                tvBottomSheetChatTitle.text = data.userName
                tvBottomSheetChatContent.text = data.content
                tvBottomSheetChatTime.text = data.getTimeHour()
            }
        }
    }
}
