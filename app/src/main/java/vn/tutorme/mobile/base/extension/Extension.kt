package vn.tutorme.mobile.base.extension

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.application.getApplication
import vn.tutorme.mobile.base.common.FlowResult
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.UiState
import vn.tutorme.mobile.base.common.exception.ApiException
import vn.tutorme.mobile.base.common.exception.HandleExceptionImpl
import vn.tutorme.mobile.base.common.loader.LoaderFactory
import vn.tutorme.mobile.base.common.utils.ToastUtils
import vn.tutorme.mobile.base.common.view.DELAY_BETWEEN_TIME_DEFAULT
import vn.tutorme.mobile.base.common.view.SingleOnClickListener
import vn.tutorme.mobile.base.common.view.SingleOnTouchClickListener
import vn.tutorme.mobile.base.screen.TutorMeActivity
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE

typealias UI_STATE = UiState.UI_STATE

object Extension {
    val STRING_DEFAULT: String = ""
    val INT_DEFAULT: Int = 0
    val LONG_DEFAULT: Long = 0L
    val COLUMN_IN_ROW_DEFAULT = 3
    val LIMIT_SIZE = 20
}

fun getAppColor(
    @ColorRes colorRes: Int,
    context: Context? = getApplication()
): Int {
    return context?.let { ContextCompat.getColor(it, colorRes) } ?: Color.TRANSPARENT
}

fun getAppDrawable(
    @DrawableRes drawableId: Int,
    context: Context? = getApplication()
): Drawable? {
    return context?.let { ContextCompat.getDrawable(it, drawableId) }
}

fun getAppDimensionPixel(@DimenRes dimenId: Int, context: Context? = getApplication()) =
    context?.resources?.getDimensionPixelSize(dimenId) ?: -1

fun getAppDimension(@DimenRes dimenId: Int, context: Context? = getApplication()) =
    context?.resources?.getDimension(dimenId) ?: -1f

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun ImageView.loadUser(
    url: String?,
    ignoreCache: Boolean = false,
    placeHolder: Drawable? = getPlaceHolderUser()
) {
    LoaderFactory.glide().loadImage(
        view = this,
        url = url,
        placeHolder = placeHolder,
        ignoreCache = ignoreCache
    )
}

private fun getPlaceHolderUser(): Drawable? {
    return getAppDrawable(R.drawable.bg_avatar_mock)
}

fun ImageView.loadImage(
    url: String?,
    ignoreCache: Boolean = false,
    placeHolder: Drawable? = getPlaceHolderDefault()
) {
    LoaderFactory.glide().loadImage(
        view = this,
        url = url,
        placeHolder = placeHolder,
        ignoreCache = ignoreCache
    )
}

fun getPlaceHolderDefault(): Drawable? {
    return getAppDrawable(R.drawable.ic_no_img_default)
}

fun getPlaceHolderImageLoading(): Drawable? {
    return getAppDrawable(R.drawable.ic_place_holder_loading)
}

fun ImageView.loadImage(drawable: Drawable?) {
    setImageDrawable(drawable)
}

fun ImageView.loadImage(
    drawable: Drawable?,
    ignoreCache: Boolean = false,
    placeHolder: Drawable? = getPlaceHolderDefault()
) {
    LoaderFactory.glide().loadImage(
        view = this,
        drawable = drawable,
        placeHolder = placeHolder,
        ignoreCache = ignoreCache
    )
}

fun ImageView.loadImage(drawable: Int) {
    setImageResource(drawable)
}

fun View.createVisibility(status: Boolean) {
    if (status) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun Fragment.toast(msg: String?) {
    context?.let {
        ToastUtils.show(it, msg, Toast.LENGTH_SHORT)
    }
}

fun Fragment.hideToast() {
    ToastUtils.hide()
}

fun TextView.setCustomFont(context: Context? = getApplication(), fontPath: Int) {
    val typeface = context?.let { ResourcesCompat.getFont(it, fontPath) }
    setTypeface(typeface)
}

fun View.setOnSafeClick(
    delayBetweenTime: Long = DELAY_BETWEEN_TIME_DEFAULT,
    onClick: ((view: View) -> Unit)
) {
    setOnClickListener(object : SingleOnClickListener(delayBetweenTime) {
        override fun onDebouncedClick(view: View) = onClick(view)
    })
}

fun TextView.setImageTextView(
    left: Drawable? = null,
    top: Drawable? = null,
    right: Drawable? = null,
    bottom: Drawable? = null
) {
    setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}

fun TextView.setOnTouchClick(
    delayBetweenTime: Long = DELAY_BETWEEN_TIME_DEFAULT,
    colorResDown: Int = R.color.gray_25,
    colorResUp: Int = R.color.gray,
    onClick: ((view: View) -> Unit)
) {
    setOnTouchListener(object : SingleOnTouchClickListener(delayBetweenTime) {
        override fun onDebouncedClick(view: View) {
            this@setOnTouchClick.setTextColor(getAppColor(colorResUp))
            onClick.invoke(view)
        }

        override fun onChangeText(view: View) {
            this@setOnTouchClick.setTextColor(getAppColor(colorResDown))
        }
    })
}

fun getAppString(
    @StringRes stringId: Int,
    context: Context? = getApplication()
): String {
    return context?.getString(stringId) ?: ""
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.data(): DATA? {
    return this.value.data
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.success(data: DATA) {
    this.value = FlowResult.newInstance<DATA>().success(data)
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.failure(throwable: Throwable, data: DATA? = null) {
    this.value = FlowResult.newInstance<DATA>().failure(throwable, data)
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.loading(message: String? = null) {
    this.value = FlowResult.newInstance<DATA>().loading(message)
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.initial() {
    this.value = FlowResult.newInstance<DATA>().initial()
}

fun <DATA> MutableStateFlow<FlowResult<DATA>>.reset() {
    this.value = FlowResult.newInstance<DATA>().reset()
}

fun <T> Flow<T>.onException(onCatch: suspend (Throwable) -> Unit): Flow<T> {
    return catch { e ->
        when (e) {
            is ApiException -> {
                e.printStackTrace()
                val exception = ApiException(e.code, HandleExceptionImpl.getMessage(e))
                onCatch(exception)
            }

            else -> {
                e.printStackTrace()
                onCatch(e)
            }
        }
    }
}

fun <T> TutorMeActivity<*>.handleUiState(
    flowResult: FlowResult<T>,
    listener: IViewListener? = null,
    canShowLoading: Boolean = false,
    canShowLoadMore: Boolean = false,
    canShowError: Boolean = true
) {
    when (flowResult.getUiState()) {
        UI_STATE.INITIAL -> {
            listener?.onInitial()
        }

        UI_STATE.LOADING -> {
            if (!canShowLoadMore) {
                if (canShowLoading) {
                    this.showLoading()
                }
            }
            listener?.onLoading()
        }

        UI_STATE.FAILURE -> {
            if (canShowLoading) {
                this.hideLoading()
            }

            listener?.onFailure()
        }

        UI_STATE.SUCCESS -> {
            if (canShowLoading) {
                this.hideLoading()
            }
            listener?.onSuccess()
        }
    }
}

fun <T> TutorMeFragment<*>.handleUiState(
    flowResult: FlowResult<T>,
    listener: IViewListener? = null,
    canShowLoading: Boolean = false,
    canShowLoadMore: Boolean = false,
    canShowError: Boolean = true
) {
    when (flowResult.getUiState()) {
        UI_STATE.INITIAL -> {
            listener?.onInitial()
        }

        UI_STATE.LOADING -> {
            if (!canShowLoadMore) {
                if (canShowLoading) {
                    this.showLoading()
                }
            }
            listener?.onLoading()
        }

        UI_STATE.FAILURE -> {
            if (canShowLoading) {
                showError(flowResult.getMessage())
                this.hideLoading()
            }

            listener?.onFailure()
        }

        UI_STATE.SUCCESS -> {
            if (canShowLoading) {
                this.hideLoading()
            }
            listener?.onSuccess()
        }
    }
}

fun <DATA> Fragment.coroutinesLaunch(
    flow: Flow<FlowResult<DATA>>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    launch: suspend (flowResult: FlowResult<DATA>) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state = state) {
            flow.collect {
                launch.invoke(it)
            }
        }
    }
}

fun SpannableStringBuilder.withSpanTextClick(
    character: String,
    action: () -> Unit,
    setTextColor: ((TextPaint) -> Unit)? = null
) {
    val clickableTextIndex = this.indexOf(character)

    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            action.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            setTextColor?.invoke(ds)
        }
    }

    this.setSpan(
        clickableSpan,
        clickableTextIndex,
        clickableTextIndex + character.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

fun isEmailValid(email: String): Boolean {
    val emailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

    return emailPattern.matches(email)
}

fun isLogin(): Boolean {
    return AppPreferences.userInfo != null
}

fun View.scaleAnimation() {
    animate()
        .scaleX(1.1f)
        .scaleY(1.1f)
        .setDuration(150)
        .setInterpolator(DecelerateInterpolator())
        .withEndAction {
            ViewCompat.animate(this)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(50).interpolator = AccelerateInterpolator()
        }
}

fun setActionRoleState(actionTeacher: () -> Unit, actionStudent: () -> Unit) {
    if (AppPreferences.userInfo?.role == ROLE_TYPE.TEACHER_TYPE) {
        actionTeacher.invoke()
    } else {
        actionStudent.invoke()
    }
}
