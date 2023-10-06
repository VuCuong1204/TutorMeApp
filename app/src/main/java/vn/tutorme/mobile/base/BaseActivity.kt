package vn.tutorme.mobile.base

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import vn.tutorme.mobile.base.common.LAYOUT_INVALID
import vn.tutorme.mobile.base.common.StatusBar
import vn.tutorme.mobile.base.common.anim.FadeAnim
import vn.tutorme.mobile.base.common.anim.IScreenAnim
import vn.tutorme.mobile.base.extension.getAppColor

abstract class BaseActivity(protected val layoutId: Int) : AppCompatActivity(), BaseView {

    protected var TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {

        if (isOnlyPortraitScreen()) {
            setPortraitScreen()
        }

        super.onCreate(savedInstanceState)

        try {
            attachView()
            onPrepareInitView()
            onInitView()
            onObserverViewModel()
        } catch (e: InflateException) {
            e.printStackTrace()
            Log.d(TAG, "${e.message}")
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            Log.d(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "${e.message}")
        }
    }

    open fun attachView() {
        setContentView(layoutId)
        setupStatusBar().let {
            setStatusColor(it.color, it.isDarkText)
        }
    }

    open fun isOnlyPortraitScreen(): Boolean = true

    open fun getContainerId(): Int = LAYOUT_INVALID

    open fun setupStatusBar(): StatusBar = StatusBar()

    fun setStatusColor(color: Int = android.R.color.black, isDarkText: Boolean = true) {
        window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                decorView.let {
                    ViewCompat.getWindowInsetsController(it)?.apply {
                        isAppearanceLightStatusBars = isDarkText
                    }
                }
            } else {
                decorView.let {
                    it.systemUiVisibility =
                        if (!isDarkText) {
                            it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                        } else {
                            it.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        }
                }
            }

            statusBarColor = getAppColor(color)
        }
    }

    //region orientation
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setPortraitScreen() {
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "${e.message}")
        }
    }

    fun navigateTo(
        clazz: Class<out BaseActivity>,
        bundle: Bundle?,
        onCallback: (Intent) -> Unit = {}
    ) {
        val intent = Intent(this, clazz)
        bundle?.let { intent.putExtras(it) }
        onCallback.invoke(intent)
        startActivity(intent)
    }

    fun addFragment(
        fragment: Fragment,
        bundle: Bundle?,
        keepToBackStack: Boolean = true,
        screenAnim: IScreenAnim = FadeAnim()
    ) {
        includeFragment(
            fragment,
            bundle,
            getContainerId(),
            false,
            keepToBackStack,
            screenAnim
        )
    }

    fun replaceFragment(
        fragment: Fragment,
        bundle: Bundle?,
        keepToBackStack: Boolean = true,
        screenAnim: IScreenAnim = FadeAnim()
    ) {
        includeFragment(
            fragment,
            bundle,
            getContainerId(),
            true,
            keepToBackStack,
            screenAnim
        )
    }

    fun clearBackStackFragment() {
        supportFragmentManager.let { fm ->
            fm.backStackEntryCount.let { count ->
                for (i in 0 until count) {
                    fm.popBackStack()
                }
            }
        }
    }

    fun clearBackStackFragment(index: Int) {
        supportFragmentManager.let { fm ->
            if (index > 0) {
                for (i in 0 until index - 1) {
                    fm.popBackStack()
                }
            }
        }
    }

    fun getCurrentFragment(): Fragment? {
        val fragmentList = supportFragmentManager.fragments
        return fragmentList.lastOrNull()
    }

    fun backFragment() {
        supportFragmentManager.popBackStack()
    }

    fun replaceFragmentState(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        val fragmentFind = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentFind == null) {
            replaceFragment(
                fragment = fragment,
                bundle = null,
                keepToBackStack = true
            )
        } else {
            replaceFragment(
                fragment = fragmentFind,
                bundle = null,
                keepToBackStack = false
            )
        }
    }

    // thay thế fragment trong backstack và xóa bỏ các fragment tại 1 vị trí cụ thể
    fun replaceFragmentInitialState(fragmentInitial: Fragment, keepIndex: Int) {
        clearBackStackFragment(supportFragmentManager.backStackEntryCount - keepIndex)
        val tag = fragmentInitial::class.java.simpleName
        val fragmentFind = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentFind == null) {
            replaceFragment(
                fragment = fragmentInitial,
                bundle = null,
                keepToBackStack = true
            )
        } else {
            replaceFragment(
                fragment = fragmentFind,
                bundle = null,
                keepToBackStack = true
            )
        }

    }

    fun replaceFragmentInitialState(fragmentInitial: Fragment) {
        val tag = fragmentInitial::class.java.simpleName
        val fragmentFind = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentFind == null) {
            replaceFragment(
                fragment = fragmentInitial,
                bundle = null,
                keepToBackStack = true
            )
        } else {
            replaceFragment(
                fragment = fragmentFind,
                bundle = null,
                keepToBackStack = true
            )
        }
    }

    // loại bỏ tất cả các Fragment cùng tag
    fun backFragment(tag: String) {
        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun includeFragment(
        fragment: Fragment,
        bundle: Bundle?,
        containerId: Int,
        isReplace: Boolean,
        keepToBackStack: Boolean,
        screenAnim: IScreenAnim
    ) {
        if (containerId == -1) {
            throw IllegalArgumentException("Cần truyền layout để có thể thục hiện replace hoặc add")
        }
        try {
            val tag = fragment::class.java.simpleName
            bundle?.let {
                fragment.arguments = it
            }
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    screenAnim.enter(),
                    screenAnim.exit(),
                    screenAnim.popEnter(),
                    screenAnim.popExit()
                )
                if (isReplace) {
                    replace(containerId, fragment, tag)
                } else {
                    add(containerId, fragment, tag)
                }

                if (keepToBackStack) {
                    addToBackStack(tag)
                }

                commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
