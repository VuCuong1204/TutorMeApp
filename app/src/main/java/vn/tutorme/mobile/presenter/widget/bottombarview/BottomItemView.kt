package vn.tutorme.mobile.presenter.widget.bottombarview

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.hide
import vn.tutorme.mobile.base.extension.scaleAnimation
import vn.tutorme.mobile.base.extension.setCustomFont
import vn.tutorme.mobile.base.extension.show

class BottomItemView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    private lateinit var clChild: ConstraintLayout
    private lateinit var tvTitle: TextView
    private lateinit var ivBackground: ImageView
    private lateinit var ivAvatarInactive: ImageView
    private lateinit var ivAvatarActive: ImageView

    private var textContent: String? = null
    private var textColorContent: Int? = null
    private var textFontContent: Typeface? = null
    private var backgroundParent: Int? = null
    private var backgroundParentShow: Boolean? = null
    private var backgroundChild: Drawable? = null
    private var backgroundChildShow: Boolean? = null
    private var iconActiveShow: Boolean? = null
    private var iconActive: Drawable? = null
    private var iconInactiveShow: Boolean? = null
    private var iconInactive: Drawable? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_item_view, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        clChild = findViewById(R.id.clBottomItemChild)
        tvTitle = findViewById(R.id.tvBottomItemTitle)
        ivBackground = findViewById(R.id.ivBottomItemBackground)
        ivAvatarInactive = findViewById(R.id.ivBottomItemAvatarInactive)
        ivAvatarActive = findViewById(R.id.ivBottomItemAvatarActive)

        textContent?.let { tvTitle.text = it }
        textColorContent?.let { tvTitle.setTextColor(it) }
        textFontContent?.let { tvTitle.typeface = it }
        backgroundParent?.let { clChild.setBackgroundColor(it) }
        backgroundParentShow?.let { if (it) clChild.show() else clChild.gone() }
        backgroundChild?.let { ivBackground.setImageDrawable(it) }
        backgroundChildShow?.let { if (it) ivBackground.show() else ivBackground.gone() }
        iconActiveShow?.let { if (it) ivAvatarActive.show() else ivAvatarActive.gone() }
        iconInactiveShow?.let { if (it) ivAvatarInactive.show() else ivAvatarInactive.gone() }
        iconActive?.let { ivAvatarActive.setImageDrawable(it) }
        iconInactive?.let { ivAvatarInactive.setImageDrawable(it) }
    }

    fun setBackGround(color: Int) {
        clChild.setBackgroundColor(color)
    }

    fun setTextContent(text: String) {
        tvTitle.text = text
    }

    fun setColorContent(color: Int) {
        tvTitle.setTextColor(color)
    }

    fun setFontContent(fontPath: Int) {
        tvTitle.setCustomFont(fontPath = fontPath)
    }

    fun setImageInActive(drawable: Drawable?) {
        ivAvatarInactive.setImageDrawable(drawable)
    }

    fun setImageInActiveState(state: Boolean) {
        if (state) ivAvatarInactive.show() else ivAvatarInactive.gone()
    }

    fun setImageActive(drawable: Drawable?) {
        ivAvatarActive.setImageDrawable(drawable)
    }

    fun setImageActiveState(state: Boolean) {
        if (state) ivAvatarActive.show() else ivAvatarActive.gone()
    }

    fun setImageBackgroundState(state: Boolean) {
        if (state) ivBackground.show() else ivBackground.gone()
    }

    fun setImageBackgroundActive(value: Int) {
        ivBackground.setImageResource(value)
    }

    fun setOnClickState(state: Boolean) {
        if (state) {
            setImageBackgroundState(true)
            setImageActiveState(true)
            setImageInActiveState(false)
            setBackGround(getAppColor(R.color.transparent))
            tvTitle.setTextColor(getAppColor(R.color.primary))
            tvTitle.setCustomFont(fontPath = R.font.font_raleway_bold)
            ivAvatarInactive.scaleAnimation()
        } else {
            ivBackground.hide()
            setImageActiveState(false)
            setImageInActiveState(true)
            setBackGround(getAppColor(R.color.white))
            tvTitle.setTextColor(getAppColor(R.color.neutral_13))
            tvTitle.setCustomFont(fontPath = R.font.font_raleway_medium)
        }
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.BottomItemView, 0, 0)

        if (ta.hasValue(R.styleable.BottomItemView_text_content)) {
            textContent = ta.getString(R.styleable.BottomItemView_text_content)
        }

        if (ta.hasValue(R.styleable.BottomItemView_text_color_content)) {
            textColorContent = ta.getColor(R.styleable.BottomItemView_text_color_content, 0)
        }

        if (ta.hasValue(R.styleable.BottomItemView_text_font_content)) {
            val fontId = ta.getResourceId(R.styleable.BottomItemView_text_font_content, -1)
            if (fontId != -1) {
                textFontContent = ResourcesCompat.getFont(context, fontId)
            }
        }

        if (ta.hasValue(R.styleable.BottomItemView_background_parent)) {
            backgroundParent = ta.getColor(R.styleable.BottomItemView_background_parent, 0)
        }

        if (ta.hasValue(R.styleable.BottomItemView_background_parent_show)) {
            backgroundParentShow = ta.getBoolean(R.styleable.BottomItemView_background_parent_show, false)
        }

        if (ta.hasValue(R.styleable.BottomItemView_background_child)) {
            backgroundChild = ta.getDrawable(R.styleable.BottomItemView_background_child)
        }

        if (ta.hasValue(R.styleable.BottomItemView_background_child_show)) {
            backgroundChildShow = ta.getBoolean(R.styleable.BottomItemView_background_child_show, false)
        }

        if (ta.hasValue(R.styleable.BottomItemView_icon_active_show)) {
            iconActiveShow = ta.getBoolean(R.styleable.BottomItemView_icon_active_show, false)
        }

        if (ta.hasValue(R.styleable.BottomItemView_icon_inactive_show)) {
            iconInactiveShow = ta.getBoolean(R.styleable.BottomItemView_icon_inactive_show, false)
        }

        if (ta.hasValue(R.styleable.BottomItemView_icon_active)) {
            iconActive = ta.getDrawable(R.styleable.BottomItemView_icon_active)
        }

        if (ta.hasValue(R.styleable.BottomItemView_icon_inactive)) {
            iconInactive = ta.getDrawable(R.styleable.BottomItemView_icon_inactive)
        }

        ta.recycle()
    }
}
