package vn.tutorme.mobile.base.common.view

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan

internal class SpannableBuilder(text: String? = null) {

    var spannedText: SpannableStringBuilder? = null
    private var mLastStartSpanIndex = 0

    init {
        spannedText = if (text != null) {
            SpannableStringBuilder(text)
        } else {
            SpannableStringBuilder("")
        }
    }

    fun appendText(text: String?): SpannableBuilder {
        mLastStartSpanIndex = spannedText!!.length
        spannedText?.append(text ?: "")
        return this
    }

    fun withSpan(span: Any?): SpannableBuilder {
        val endIndex = spannedText!!.length
        spannedText?.setSpan(
            span, mLastStartSpanIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun withSpanImage(drawable: Drawable?): SpannableBuilder {
        mLastStartSpanIndex = spannedText!!.length
        val endIndex = mLastStartSpanIndex + 1
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val imageSpan = drawable?.let { ImageSpan(it, ImageSpan.ALIGN_CENTER) }
        spannedText?.append(" ")
        spannedText?.setSpan(
            imageSpan,
            mLastStartSpanIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }
}
