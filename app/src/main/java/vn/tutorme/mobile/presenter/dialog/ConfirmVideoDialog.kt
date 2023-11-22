package vn.tutorme.mobile.presenter.dialog

import android.media.MediaPlayer
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeDialog
import vn.tutorme.mobile.databinding.ConfirmVideoDialogBinding

class ConfirmVideoDialog : TutorMeDialog<ConfirmVideoDialogBinding>(R.layout.confirm_video_dialog) {

    private var mediaPlayer: MediaPlayer? = null
    var listener: IConfirmVideoListener? = null
    var userName: String? = null


    override fun getBackgroundId() = R.id.flConfirmVideoRoot

    override fun screen() = DialogScreen().apply {
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = false
        isDismissByOnBackPressed = false
    }

    override fun onInitView() {
        super.onInitView()

        mediaPlayer = MediaPlayer.create(this@ConfirmVideoDialog.context, R.raw.ring_phone_video_call)

        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.start()
        }

        mediaPlayer?.start()

        userName?.let { binding.tvConfirmVideoUserName.text = it }
        binding.ivConfirmVideoAccept.setOnSafeClick {
            dismiss()
            listener?.onConfirmClick()
        }

        binding.ivConfirmVideoReject.setOnSafeClick {
            dismiss()
            listener?.onRejectClick()
        }
    }

    override fun onDestroyView() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroyView()
    }

    interface IConfirmVideoListener {
        fun onRejectClick()
        fun onConfirmClick()
    }
}
