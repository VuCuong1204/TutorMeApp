package vn.tutorme.mobile.base.common.view

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class FontSpan(typeface: Typeface?, family: String? = "") : TypefaceSpan(family) {

    var newTypeface: Typeface? = null

    init {
        newTypeface = typeface
    }

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, newTypeface!!)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, newTypeface!!)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val old: Typeface = paint.typeface
        oldStyle = old.style
        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }
        paint.typeface = tf
    }
}
