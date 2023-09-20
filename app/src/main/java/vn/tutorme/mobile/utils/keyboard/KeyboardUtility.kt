package vn.tutorme.mobile.utils.keyboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity


class KeyboardUtility {
    companion object {
        fun showKeyBoard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun dontShowKeyboard(activity: Activity) {
            try {
                activity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                hideSoftKeyboard(activity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideSoftKeyboard(activity: Activity) {
            try {
                val inputMethodManager: InputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideSoftKeyboard(activity: FragmentActivity) {
            try {
                val inputMethodManager: InputMethodManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun hideSoftKeyboard(context: Context, view: View) {
            try {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
                imm!!.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun hideSoftKeyboard(context: Context, editText: EditText) {
            try {
                val inputMethodManager: InputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun isKeyboardShown(context: Context, editText: EditText): Boolean {
            val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            return imm != null && imm.isActive(editText)
        }

        @SuppressLint("ClickableViewAccessibility")
        fun hideSoftKeyboard(activity: Activity, view: View) {
            try {
                if (view !is EditText) {
                    view.setOnTouchListener { _, _ ->
                        hideSoftKeyboard(activity)
                        false
                    }
                }

                if (view is ViewGroup) {
                    for (i in 0 until view.childCount) {
                        hideSoftKeyboard(activity, view.getChildAt(i))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun hideSoftKeyboard(context: Context, view: View, onHide: (() -> Unit)? = null) {
            try {
                if (view !is EditText) {
                    view.setOnTouchListener { v, _ ->
                        hideSoftKeyboard(context, v)
                        onHide?.invoke()
                        false
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
