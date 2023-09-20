package vn.tutorme.mobile.presenter.widget.background

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import vn.tutorme.mobile.R

class CircleInSideView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.circle_inside_view, this, true)
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleInSideView, 0, 0)


        ta.recycle()
    }

}
