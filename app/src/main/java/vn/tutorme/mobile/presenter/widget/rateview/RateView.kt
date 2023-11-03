package vn.tutorme.mobile.presenter.widget.rateview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import vn.tutorme.mobile.R

class RateView(ctx: Context, attrs: AttributeSet?) : LinearLayout(ctx, attrs) {

    private lateinit var tvTitle: TextView
    private lateinit var sbCourse: SeekBar
    private lateinit var edtComment: EditText

    private var title: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.rate_view, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        tvTitle = findViewById(R.id.tvRateViewTitle)
        sbCourse = findViewById(R.id.sbRateViewScore)
        edtComment = findViewById(R.id.edtRateViewComment)

        title?.let { tvTitle.text = it }
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.RateView, 0, 0)

        if (ta.hasValue(R.styleable.RateView_title)) {
            title = ta.getString(R.styleable.RateView_title)
        }

        ta.recycle()
    }

    fun getContent(): String {
        return edtComment.text.toString().trim()
    }
}
