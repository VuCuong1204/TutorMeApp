package vn.tutorme.mobile.presenter.dialog

import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.BaseBindingDialog
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.SelectImagePhotoDialogBinding

class SelectImageDialog(private val selectImage: ISelectImage) : BaseBindingDialog<SelectImagePhotoDialogBinding>(R.layout.select_image_photo_dialog) {
    //TODO viết dialog builder

    override fun getBackgroundId() = R.id.constSelectImageDialog

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.NORMAL
        isFullWidth = true
        isFullHeight = true
        isDismissByTouchOutSide = true
        isDismissByOnBackPressed = true
    }

    override fun onInitView() {
        binding.tvDialogGallery.setOnSafeClick {
            selectImage.fromGallery()
            dismiss()
        }

        binding.tvDialogTakePhoto.setOnSafeClick {
            selectImage.takePhoto()
            dismiss()
        }
    }

    interface ISelectImage {
        fun fromGallery()
        fun takePhoto()
    }
}
