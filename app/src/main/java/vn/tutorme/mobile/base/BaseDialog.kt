package vn.tutorme.mobile.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.common.LAYOUT_INVALID

abstract class BaseDialog(@LayoutRes val layoutId: Int) : DialogFragment(), BaseView {
    protected val TAG = this::class.java
    protected lateinit var myInflater: LayoutInflater
    protected lateinit var viewRoot: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepareInitView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::myInflater.isInitialized) {
            myInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        viewRoot = attachView(inflater, container, savedInstanceState)
        return viewRoot
    }

    override fun onPrepareInitView() {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val background: View = view.findViewById(getBackground())
        background.setOnClickListener {
            if (screen().isDismissByTouchOutSide) {
                dismissDialog()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout = RelativeLayout(requireContext())
        layout.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val dialog = object : Dialog(requireContext()) {
            override fun onBackPressed() {
                if (screen().isDismissByOnBackPressed) {
                    dismissDialog()
                } else {
                    activity?.onBackPressed()
                }
            }
        }

        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layout)
            val window = dialog.window
            window?.let { w ->
                w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val wlp = w.attributes
                if (screen().isFullWidth) {
                    wlp.width = WindowManager.LayoutParams.MATCH_PARENT
                }
                if (screen().isFullHeight) {
                    wlp.height = WindowManager.LayoutParams.MATCH_PARENT
                }
                if (screen().mode == DialogScreen.DIALOG_MODE.BOTTOM) {
                    wlp.gravity = Gravity.BOTTOM
                }
            }
        }

        dialog.setCanceledOnTouchOutside(screen().isDismissByTouchOutSide)
        return dialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    open fun attachView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return myInflater.inflate(layoutId, container, false)
    }

    open fun getBackground(): Int = LAYOUT_INVALID

    open fun screen(): DialogScreen = DialogScreen()

    private fun showDialog(fm: FragmentManager, tag: String) {
        if (!this.isAdded) {
            show(fm, tag)
        }
    }

    private fun dismissDialog() {
        if (this.isAdded) {
            dismiss()
        }
    }
}
