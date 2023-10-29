package vn.tutorme.mobile.presenter.dialog.bottomsheetchat

import androidx.core.widget.addTextChangedListener
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.BottomSheetChatDialogBinding

class BottomSheetChatDialog : TutorMeDialog<BottomSheetChatDialogBinding>(R.layout.bottom_sheet_chat_dialog) {

    var lessonId: Int? = null

    var dataList = mockDataChatRoomInfoDisplay()

    private val bottomSheetChatAdapter by lazy {
        BottomSheetChatAdapter()
    }

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = true
        isDismissByOnBackPressed = true
    }

    override fun getBackgroundId(): Int = R.id.llBottomSheetChatRoot

    override fun onInitView() {
        super.onInitView()

        binding.tvBottomSheetChatCount.text = String.format(getAppString(R.string.count_commenter), dataList.size)

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
            binding.edtBottomSheetChatContent.setText("")
            binding.cvBottomSheetChatRoot.smoothScrollToPosition(0)
        }

        binding.cvBottomSheetChatRoot.apply {
            setBaseLayoutManager(layout = LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL, isReverse = true)
            setBaseAdapter(bottomSheetChatAdapter)
            submitList(dataList)
        }
    }
}
