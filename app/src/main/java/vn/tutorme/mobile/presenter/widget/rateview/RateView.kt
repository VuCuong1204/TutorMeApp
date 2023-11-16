package vn.tutorme.mobile.presenter.widget.rateview

import android.content.Context
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.view.SpannableBuilder
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.domain.model.authen.EVALUATE_STATE

class RateView(ctx: Context, attrs: AttributeSet?) : LinearLayout(ctx, attrs) {

    companion object {
        const val LIMIT_VALUE = 10f
    }

    var listener: IRateListener? = null

    private lateinit var tvTitle: TextView
    private lateinit var sbCourse: SeekBar
    private lateinit var edtComment: EditText
    private lateinit var tvComment: TextView
    private lateinit var llScore: LinearLayout

    private var title: String? = null
    private var rateType = EVALUATE_STATE.NO_EVALUATE_STATE
    private var course: Float? = null
    private var comment: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.rate_view, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        tvTitle = findViewById(R.id.tvRateViewTitle)
        sbCourse = findViewById(R.id.sbRateViewScore)
        edtComment = findViewById(R.id.edtRateViewComment)
        tvComment = findViewById(R.id.tvRateViewComment)
        llScore = findViewById(R.id.llRateViewScore)

        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val text = SpannableBuilder()
                    .appendText("$title: ")
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.text6)))
                    .appendText("${(p1.toFloat() / LIMIT_VALUE)}")
                    .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
                    .spannedText

                tvTitle.text = text
                listener?.changeSeekbar(p1.div(LIMIT_VALUE))
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        }

        edtComment.imeOptions = EditorInfo.IME_ACTION_DONE
        sbCourse.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.RateView, 0, 0)

        if (ta.hasValue(R.styleable.RateView_title)) {
            title = ta.getString(R.styleable.RateView_title)
        }

        ta.recycle()
    }

    private fun setEvaluateState() {
        if (rateType == EVALUATE_STATE.NO_EVALUATE_STATE) {
            edtComment.show()
            sbCourse.show()
            tvComment.gone()
            llScore.show()
        } else {
            edtComment.gone()
            sbCourse.gone()
            tvComment.show()
            llScore.gone()
        }
    }

    fun getContent(): String {
        return edtComment.text.toString().trim()
    }

    fun setInfoRate(rateType: EVALUATE_STATE, course: Float?, comment: String?) {
        this.rateType = rateType
        this.course = course
        this.comment = comment
        setEvaluateState()
        title?.let { tvTitle.text = it }
        val text = SpannableBuilder()
            .appendText("$title: ")
            .withSpan(ForegroundColorSpan(getAppColor(R.color.text6)))
            .appendText("${course ?: 0}")
            .withSpan(ForegroundColorSpan(getAppColor(R.color.primary)))
            .spannedText

        tvTitle.text = text
        comment?.let { tvComment.text = it }
    }
}

interface IRateListener {
    fun changeSeekbar(score: Float)
}
