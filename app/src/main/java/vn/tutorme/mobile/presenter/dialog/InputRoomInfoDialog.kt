package vn.tutorme.mobile.presenter.dialog

import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.InputRoomInfoDialogBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE

class InputRoomInfoDialog : TutorMeDialog<InputRoomInfoDialogBinding>(R.layout.input_room_info_dialog) {

    var roomId: String = STRING_DEFAULT
    var roomPassword: String = STRING_DEFAULT
    var listener: IInputRoomInfoListener? = null

    override fun getBackgroundId(): Int = R.id.flInputRoomInfoRoot

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = true
    }

    override fun onInitView() {
        super.onInitView()

        binding.edtInputRoomInfoId.setText(roomId)
        binding.edtInputRoomInfoPassword.setText(roomPassword)
        binding.tvInputRoomInfoId.text = roomId
        binding.tvInputRoomInfoPassword.text = roomPassword

        if (AppPreferences.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) {
            binding.tvInputRoomInfoSend.show()
            binding.tvInputRoomInfoEdit.show()
            if (roomId.isEmpty() || roomPassword.isEmpty()) {
                checkRoomInfoState(true)
            } else {
                checkRoomInfoState(false)
            }
        } else {
            binding.tvInputRoomInfoSend.gone()
            binding.tvInputRoomInfoEdit.gone()
            binding.edtInputRoomInfoId.gone()
            binding.edtInputRoomInfoPassword.gone()
        }

        binding.tvInputRoomInfoSend.setOnSafeClick {
            binding.edtInputRoomInfoPassword.setText(roomId)
            binding.edtInputRoomInfoPassword.setText(roomPassword)
            checkRoomInfoState(true)
        }

        binding.tvInputRoomInfoSend.setOnSafeClick {

            if (
                roomId != binding.edtInputRoomInfoId.text.toString().trim() ||
                roomPassword != binding.edtInputRoomInfoPassword.text.toString().trim()
            ) {
                dismiss()
                listener?.onSendClick(roomId, roomPassword)
            }

            roomId = binding.edtInputRoomInfoId.text.toString().trim()
            roomPassword = binding.edtInputRoomInfoPassword.text.toString().trim()

            binding.tvInputRoomInfoId.text = roomId
            binding.tvInputRoomInfoPassword.text = roomPassword

            checkRoomInfoState(false)
        }

        binding.tvInputRoomInfoEdit.setOnSafeClick {
            binding.edtInputRoomInfoId.setText(roomId)
            binding.edtInputRoomInfoPassword.setText(roomPassword)
            checkRoomInfoState(true)
        }

        binding.ivInputRoomInfoClose.setOnSafeClick {
            dismiss()
        }
    }

    private fun checkRoomInfoState(state: Boolean) {
        if (state) {
            binding.edtInputRoomInfoId.show()
            binding.edtInputRoomInfoPassword.show()
            binding.tvInputRoomInfoId.gone()
            binding.tvInputRoomInfoPassword.gone()
            binding.tvInputRoomInfoSend.apply {
                background = getAppDrawable(R.drawable.ripple_bg_primary_corner_16)
                isEnabled = true
            }
            binding.tvInputRoomInfoEdit.isEnabled = false
        } else {
            binding.edtInputRoomInfoId.gone()
            binding.edtInputRoomInfoPassword.gone()
            binding.tvInputRoomInfoId.show()
            binding.tvInputRoomInfoPassword.show()
            binding.tvInputRoomInfoSend.apply {
                background = getAppDrawable(R.drawable.shape_bg_gray_corner_16)
                isEnabled = false
            }
            binding.tvInputRoomInfoEdit.isEnabled = true
        }
    }

    interface IInputRoomInfoListener {
        fun onSendClick(id: String, password: String)
    }
}
