package vn.tutorme.mobile.presenter.widget.bottombarview

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE

class BottomMainView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    companion object {
        const val PERCENT_TEACHER_DEFAULT = 0.25f
        const val PERCENT_STUDENT_DEFAULT = 0.333f
    }

    private lateinit var btvHome: BottomItemView
    private lateinit var btvClass: BottomItemView
    private lateinit var btvNotify: BottomItemView
    private lateinit var btvProfile: BottomItemView

    private var currentState: SELECTED_STATE? = null
    private var onClick: ((SELECTED_STATE) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_main_view, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        btvHome = findViewById(R.id.btvBottomMainHome)
        btvClass = findViewById(R.id.btvBottomMainClass)
        btvNotify = findViewById(R.id.btvBottomMainNotify)
        btvProfile = findViewById(R.id.btvBottomMainProfile)

        addEventOnClick()
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }

    fun setOnTabClick(action: ((SELECTED_STATE) -> Unit)) {
        onClick = action
    }

    private fun addEventOnClick() {
        btvHome.setOnSafeClick {
            if (currentState != SELECTED_STATE.HOME) {
                changeSelectedState(SELECTED_STATE.HOME)
                btvNotify.showNotifyCountState(false)
                onClick?.invoke(SELECTED_STATE.HOME)
            }
        }

        btvClass.setOnSafeClick {
            if (currentState != SELECTED_STATE.CLASS) {
                changeSelectedState(SELECTED_STATE.CLASS)
                btvNotify.showNotifyCountState(false)
                onClick?.invoke(SELECTED_STATE.CLASS)
            }
        }

        btvNotify.setOnSafeClick {
            if (currentState != SELECTED_STATE.NOTIFICATION) {
                changeSelectedState(SELECTED_STATE.NOTIFICATION)
                btvNotify.showNotifyCountState(true)
                onClick?.invoke(SELECTED_STATE.NOTIFICATION)
            }
        }

        btvProfile.setOnSafeClick {
            if (currentState != SELECTED_STATE.PROFILE) {
                changeSelectedState(SELECTED_STATE.PROFILE)
                btvNotify.showNotifyCountState(false)
                onClick?.invoke(SELECTED_STATE.PROFILE)
            }
        }
    }

    private fun setDefaultState(currentState: SELECTED_STATE) {
        when (currentState) {
            SELECTED_STATE.HOME -> {
                btvHome.setOnClickState(true)
            }

            SELECTED_STATE.CLASS -> {
                btvClass.setOnClickState(true)
            }

            SELECTED_STATE.NOTIFICATION -> {
                btvNotify.setOnClickState(true)
            }

            SELECTED_STATE.PROFILE -> {
                btvProfile.setOnClickState(true)
            }
        }
    }

    fun changeSelectedState(state: SELECTED_STATE) {
        btvHome.setOnClickState(false)
        btvClass.setOnClickState(false)
        btvNotify.setOnClickState(false)
        btvProfile.setOnClickState(false)

        this@BottomMainView.currentState = state
        setDefaultState(state)
    }

    fun setCountNotification(count: Int) {
        btvNotify.setCountNotification(count)
        if (currentState == SELECTED_STATE.NOTIFICATION) {
            btvNotify.showNotifyCountState(true)
        } else {
            btvNotify.showNotifyCountState(false)
        }
    }

    fun setBottomBarType(roleType: ROLE_TYPE) {
        val percent = if (roleType == ROLE_TYPE.TEACHER_TYPE) {
            btvClass.show()
            PERCENT_TEACHER_DEFAULT
        } else {
            btvClass.gone()
            PERCENT_STUDENT_DEFAULT
        }

        val layoutHome = btvHome.layoutParams as LayoutParams
        layoutHome.matchConstraintDefaultWidth = LayoutParams.MATCH_CONSTRAINT_SPREAD
        layoutHome.matchConstraintPercentWidth = percent
        btvHome.layoutParams = layoutHome

        val layoutNotify = btvNotify.layoutParams as LayoutParams
        layoutNotify.matchConstraintDefaultWidth = LayoutParams.MATCH_CONSTRAINT_SPREAD
        layoutNotify.matchConstraintPercentWidth = percent
        btvNotify.layoutParams = layoutNotify

        val layoutProfile = btvProfile.layoutParams as LayoutParams
        layoutProfile.matchConstraintDefaultWidth = LayoutParams.MATCH_CONSTRAINT_SPREAD
        layoutProfile.matchConstraintPercentWidth = percent
        btvProfile.layoutParams = layoutProfile
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.BottomMainView, 0, 0)

        ta.recycle()
    }
}
