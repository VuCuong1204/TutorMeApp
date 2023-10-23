package vn.tutorme.mobile.base.common.loader.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import vn.tutorme.mobile.base.common.loader.image.CORNER_TYPE

interface IImageLoader {
    fun loadImage(
        view: ImageView,
        url: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadImage(
        view: ImageView,
        url: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false,
        loadSuccess: (()->Unit)? = null,
        loadFail: (()-> Unit)? = null
    )

    fun loadImage(
        view: ImageView,
        drawable: Drawable?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadImageBase64(
        view: ImageView,
        base64: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false
    )

    fun loadRoundCornerImage(
        view: ImageView,
        url: String?,
        corner: Int,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false,
        cornerType: CORNER_TYPE = CORNER_TYPE.ALL,
    )

    fun loadCircleImage(
        view: ImageView,
        url: String?,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false,
    )

    fun loadGif(
        view: ImageView,
        gif: Int,
        placeHolder: Drawable? = null,
        ignoreCache: Boolean = false,
    )

    fun loadImageRotate(
        view: ImageView,
        url: String?,
        placeHolder: Drawable?,
        rotate: BitmapTransformation?,
        ignoreCache: Boolean = true
    )
}
