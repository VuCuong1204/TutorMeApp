package vn.tutorme.mobile.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import vn.tutorme.mobile.base.common.StatusBar
import vn.tutorme.mobile.base.common.anim.FadeAnim
import vn.tutorme.mobile.base.common.anim.IScreenAnim

abstract class BaseFragment(@LayoutRes protected val layoutId: Int) : Fragment(), BaseView {
    protected val TAG = this::class.java.simpleName
    private val baseActivity by lazy {
        requireActivity() as BaseActivity
    }
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
            myInflater = LayoutInflater.from(requireActivity())
        }

        viewRoot = attachView(inflater, container, savedInstanceState)
        onInitView()
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onObserverViewModel()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        setupStatusBar().let {
            setStatusColor(it.color, it.isDarkText)
        }
    }

    open fun setupStatusBar(): StatusBar {
        return baseActivity.setupStatusBar()
    }

    open fun getContainerId(): Int = -1

    open fun onBackPressByFragment() {}

    open fun attachView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutId, container, false)
    }

    fun setStatusColor(color: Int = android.R.color.black, isDarkText: Boolean = true) {
        baseActivity.setStatusColor(color, isDarkText)
    }

    fun replaceFragmentState(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        val fragmentFind = childFragmentManager.findFragmentByTag(tag)
        if (fragmentFind == null) {
            replaceFragmentInsideFragment(
                fragment = fragment,
                bundle = null,
                keepToBackStack = true
            )
        } else {
            replaceFragmentInsideFragment(
                fragment = fragmentFind,
                bundle = null,
                keepToBackStack = false
            )
        }
    }

    fun replaceFragment(
        fragment: BaseFragment,
        bundle: Bundle? = null,
        keepToBackStack: Boolean = true,
        screenAnim: IScreenAnim
    ) {
        baseActivity.replaceFragment(
            fragment,
            bundle,
            keepToBackStack,
            screenAnim
        )
    }

    fun backFragment() {
        baseActivity.backFragment()
    }

    fun addFragment(
        fragment: BaseFragment,
        bundle: Bundle?,
        keepToBackStack: Boolean,
        screenAnim: IScreenAnim = FadeAnim()
    ) {
        baseActivity.addFragment(
            fragment,
            bundle,
            keepToBackStack,
            screenAnim
        )
    }

    fun replaceFragmentInsideFragment(
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

    fun addFragmentInsideFragment(
        fragment: Fragment,
        bundle: Bundle?,
        keepToBackStack: Boolean = true,
        screenAnim: IScreenAnim
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
            childFragmentManager.beginTransaction().apply {
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
