package vn.tutorme.mobile.base.common.activityresultlauncher

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import vn.tutorme.mobile.base.common.screenstate.IDisplayState
import vn.tutorme.mobile.base.screen.TutorMeActivity
import vn.tutorme.mobile.base.screen.TutorMeFragment

abstract class IActivityResult<I, O> {

    private var launcher: ActivityResultLauncher<I>? = null
    private var callback: ((O) -> Unit)? = null

    protected abstract fun getActivityContract(): ActivityResultContract<I, O>

    /**
     * Must call before onCreate in activity or fragment
     */
    fun register(ctx: IDisplayState) {
        when (ctx) {
            is TutorMeActivity<*> -> {
                launcher = ctx.registerForActivityResult(getActivityContract()) {
                    callback?.invoke(it)
                }
            }

            is TutorMeFragment<*> -> {
                launcher = ctx.registerForActivityResult(getActivityContract()) {
                    callback?.invoke(it)
                }
            }
        }
    }

    fun unregister() {
        launcher = null
    }

    fun launch(input: I? = null, callback: ((O) -> Unit) = {}) {
        this.callback = callback
        launcher?.launch(input)
    }
}
