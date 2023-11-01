package vn.tutorme.mobile.presenter.widget.detectview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class DetectView constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val redPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val transparentPaint = Paint().apply {
        color = Color.TRANSPARENT
        isAntiAlias = true
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var rect: Rect? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (rect != null) {
            canvas.drawRect(rect!!, redPaint)
        } else {
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), transparentPaint)
        }
    }

    fun setRect(rect: Rect?) {
        this.rect = rect
        postInvalidate()
    }
}
