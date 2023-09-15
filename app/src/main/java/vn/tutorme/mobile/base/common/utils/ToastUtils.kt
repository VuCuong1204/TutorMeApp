package vn.tutorme.mobile.base.common.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtils {
    private var toast: Toast? = null
    fun show(context: Context, msg: String?, time: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, time)
        toast?.show()
    }

    fun show(context: Context, @StringRes resId: Int, time: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, resId, time)
        toast?.show()
    }

    fun hide() {
        toast?.cancel()
    }
}
