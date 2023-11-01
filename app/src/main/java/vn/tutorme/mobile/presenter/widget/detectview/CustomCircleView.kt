package vn.tutorme.mobile.presenter.widget.detectview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View

class CustomCircleView constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private lateinit var bitmap: Bitmap
    private lateinit var cvs: Canvas
    private lateinit var pBitmap: Paint

    private val transparentPaint = Paint().apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val semiTransparentPaint = Paint().apply {
        color = Color.argb(150, 0, 0, 0)
    }

    private val circlePaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = 15f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val circlePaintBlue = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        strokeWidth = 15f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val circlePaintWhite = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        strokeWidth = 15f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var isSuccess = DETECT_STATE.NONE

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw && h != oldh) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            cvs = Canvas(bitmap)
            pBitmap = Paint()
            cvs.drawRect(0f, 0f, width.toFloat(), height.toFloat(), semiTransparentPaint)
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2).toFloat()

        when (isSuccess) {
            DETECT_STATE.SUCCESS -> {
                cvs.drawCircle(centerX, centerY, radius - RADIUS_CIRCLE_DEFAULT, circlePaintBlue)
            }

            DETECT_STATE.FAILED -> {
                cvs.drawCircle(centerX, centerY, radius - RADIUS_CIRCLE_DEFAULT, circlePaint)
            }

            else -> {
                cvs.drawCircle(centerX, centerY, radius - RADIUS_CIRCLE_DEFAULT, circlePaintWhite)
            }
        }

        cvs.drawCircle(centerX, centerY, radius - RADIUS_DEFAULT, transparentPaint)

        canvas.drawBitmap(bitmap, 0f, 0f, pBitmap)
    }

    fun setCircleState(isSuccess: DETECT_STATE) {
        this.isSuccess = isSuccess
        invalidate()
    }
}

const val RADIUS_CIRCLE_DEFAULT = 52f
const val RADIUS_DEFAULT = 50f

enum class DETECT_STATE {
    SUCCESS, NONE, FAILED
}
