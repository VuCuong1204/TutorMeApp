package vn.tutorme.mobile.presenter.widget.textfield

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.extension.show
import vn.tutorme.mobile.utils.keyboard.KeyboardUtility

class TextFieldView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    private lateinit var clRoot: ConstraintLayout
    private lateinit var tvTitle: TextView
    private lateinit var edtContent: EditText
    private lateinit var ivRight: ImageView
    private lateinit var tvDescription: TextView

    private var textTitle: String? = null
    private var textHint: String? = null
    private var rightIcon: Drawable? = null
    private var paddingRightIcon: Int? = null
    private var bgRoot: Drawable? = null

    private var inputType: INPUT_TYPE = INPUT_TYPE.TEXT_TYPE
    private var isPasswordVisible: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.text_field_view, this, true)
        initView(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        clRoot = findViewById(R.id.clTextFieldRoot)
        tvTitle = findViewById(R.id.tvTextFieldTitle)
        edtContent = findViewById(R.id.edtTextFieldContent)
        ivRight = findViewById(R.id.ivTextFieldRight)
        tvDescription = findViewById(R.id.tvTextFieldDescription)

        rightIcon?.let { ivRight.setImageDrawable(it) }
        bgRoot?.let { clRoot.background = it }
        textHint?.let { edtContent.hint = it }
        textTitle?.let { tvTitle.text = it }
        paddingRightIcon?.let { ivRight.setPadding(it, it, it, it) }

        edtContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setEditTextFocusState(true)
                edtContent.hint = getAppString(R.string.fill_in_here)
                clRoot.background = getAppDrawable(R.drawable.shape_bg_white_corner_14_stroke_2_bg_primary)
            } else {
                setEditTextFocusState(false)
                edtContent.hint = textHint
                clRoot.background = if (bgRoot == null) getAppDrawable(R.drawable.shape_bg_white_corner_14) else bgRoot
            }
        }

        edtContent.addTextChangedListener {
            setTextDescriptionState(false)
            if (it?.trim().isNullOrEmpty()) {
                if (inputType == INPUT_TYPE.TEXT_TYPE) ivRight.gone()
            } else {
                if (inputType == INPUT_TYPE.TEXT_TYPE) ivRight.show()
            }
        }

        edtContent.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                edtContent.clearFocus()
                KeyboardUtility.hideSoftKeyboard(context, edtContent)
                true
            } else {
                false
            }
        }

        setEventSelectIconRight()
    }

    fun setInputType(type: INPUT_TYPE) {
        this.inputType = type
        when (type) {
            INPUT_TYPE.TEXT_TYPE -> {
                edtContent.inputType = InputType.TYPE_CLASS_TEXT
            }

            INPUT_TYPE.TEXT_PASSWORD_TYPE -> {
                ivRight.show()
                edtContent.apply {
                    inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    transformationMethod = BiggerDotPasswordTransformationMethod
                }
            }
        }
    }

    fun setImgOptions(imeOptions: Int) {
        edtContent.imeOptions = imeOptions
    }

    fun setTextDescription(charSequence: CharSequence) {
        tvDescription.text = charSequence
    }

    fun setTextContent(value: String) {
        edtContent.setText(value)
    }

    fun setTextDescriptionColor(color: Int) {
        tvDescription.setTextColor(color)
    }

    fun setTextDescriptionState(state: Boolean) {
        if (state) tvDescription.show() else tvDescription.gone()
    }

    fun getTextContent(): String {
        return edtContent.text.toString().trim()
    }

    fun setBackgroundRoot(drawable: Drawable?) {
        clRoot.background = drawable
    }

    fun showError(value: CharSequence) {
        setTextDescription(value)
        setTextDescriptionColor(getAppColor(R.color.red_20))
        setBackgroundRoot(getAppDrawable(R.drawable.shape_bg_white_corner_14_stroke_2_bg_red20))
        setTextDescriptionState(true)
    }

    private fun setEventSelectIconRight() {
        ivRight.setOnSafeClick {
            when (inputType) {
                INPUT_TYPE.TEXT_TYPE -> {
                    edtContent.setText("")
                }

                INPUT_TYPE.TEXT_PASSWORD_TYPE -> {
                    val cursorPosition = edtContent.selectionStart
                    if (isPasswordVisible) {
                        ivRight.setImageDrawable(getAppDrawable(R.drawable.ic_eye_gone))
                        edtContent.inputType = (InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT)
                        edtContent.transformationMethod = BiggerDotPasswordTransformationMethod
                    } else {
                        ivRight.setImageDrawable(getAppDrawable(R.drawable.ic_eye_show))
                        edtContent.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        edtContent.inputType = (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT)
                    }

                    edtContent.setSelection(cursorPosition)
                    isPasswordVisible = !isPasswordVisible
                }
            }
        }
    }

    private fun setEditTextFocusState(state: Boolean) {
        val params = edtContent.layoutParams as MarginLayoutParams
        if (state) {
            params.setMargins(
                getAppDimension(R.dimen.fbase_dimen_14).toInt(),
                getAppDimension(R.dimen.fbase_dimen_22).toInt(),
                getAppDimension(R.dimen.fbase_dimen_26).toInt(),
                getAppDimension(R.dimen.fbase_dimen_6).toInt()
            )
            tvTitle.show()
        } else {
            params.setMargins(
                getAppDimension(R.dimen.fbase_dimen_14).toInt(),
                getAppDimension(R.dimen.fbase_dimen_14).toInt(),
                getAppDimension(R.dimen.fbase_dimen_26).toInt(),
                getAppDimension(R.dimen.fbase_dimen_14).toInt()
            )
            tvTitle.gone()
        }
        edtContent.layoutParams = params
    }

    private fun initView(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.TextFieldView, 0, 0)

        if (ta.hasValue(R.styleable.TextFieldView_right_icon)) {
            rightIcon = ta.getDrawable(R.styleable.TextFieldView_right_icon)
        }

        if (ta.hasValue(R.styleable.TextFieldView_background_tf)) {
            bgRoot = ta.getDrawable(R.styleable.TextFieldView_background_tf)
        }

        if (ta.hasValue(R.styleable.TextFieldView_text_title)) {
            textTitle = ta.getString(R.styleable.TextFieldView_text_title)
        }

        if (ta.hasValue(R.styleable.TextFieldView_text_hint)) {
            textHint = ta.getString(R.styleable.TextFieldView_text_hint)
        }

        if (ta.hasValue(R.styleable.TextFieldView_padding_right_icon)) {
            paddingRightIcon = ta.getDimensionPixelOffset(R.styleable.TextFieldView_padding_right_icon, 0)
        }

        ta.recycle()
    }
}

enum class INPUT_TYPE {
    TEXT_TYPE,
    TEXT_PASSWORD_TYPE
}
