package vn.tutorme.mobile.presenter.widget.background

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.zxing.Dimension
import vn.tutorme.mobile.R

class CircleInSideView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    private lateinit var ivAvatar: ImageView

    private var iconCenter: Drawable? = null
    private var iconCenterPadding: Int? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.circle_inside_view, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        ivAvatar = findViewById(R.id.ivCircleInsideAvatar)

        iconCenter?.let {
            ivAvatar.setImageDrawable(it)
        }

        iconCenterPadding?.let {
            ivAvatar.setPadding(it, it, it, it)
        }
    }

    fun setAvatar(drawable: Drawable?) {
        ivAvatar.setImageDrawable(drawable)
    }

    fun setPaddingAvatar(padding: Int) {
        ivAvatar.setPadding(padding, padding, padding, padding)
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleInSideView, 0, 0)

        if (ta.hasValue(R.styleable.CircleInSideView_center_icon)) {
            iconCenter = ta.getDrawable(R.styleable.CircleInSideView_center_icon)
        }

        if (ta.hasValue(R.styleable.CircleInSideView_center_icon_padding)) {
            iconCenterPadding = ta.getDimensionPixelOffset(R.styleable.CircleInSideView_center_icon_padding, 0)
        }

        ta.recycle()
    }
}
