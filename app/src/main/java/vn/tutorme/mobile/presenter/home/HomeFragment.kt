package vn.tutorme.mobile.presenter.home

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.HomeFragmentBinding
import vn.tutorme.mobile.domain.model.authen.ROLE_TYPE
import vn.tutorme.mobile.presenter.classall.ClassAllFragment
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog
import vn.tutorme.mobile.presenter.lessonall.LessonAllFragment
import vn.tutorme.mobile.presenter.lessonevaluate.LessonEvaluateFragment
import vn.tutorme.mobile.presenter.registerclass.ClassWaitingConfirmFragment

@AndroidEntryPoint
class HomeFragment : TutorMeFragment<HomeFragmentBinding>(R.layout.home_fragment) {

    private val viewModel by viewModels<HomeViewModel>()
    private val homeAdapter by lazy { HomeAdapter() }

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
        addListener()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        coroutinesLaunch(viewModel.homeState) {
            handleUiState(it, object : IViewListener {

                override fun onLoading() {
                    binding.cvHomeRoot.showLoading()
                }

                override fun onFailure() {
                    binding.cvHomeRoot.hideLoading()
                    binding.srlHomeRoot.isRefreshing = false
                }

                override fun onSuccess() {
                    binding.cvHomeRoot.submitList(it.data?.dataList)
                    viewModel.resetState()
                    binding.cvHomeRoot.hideLoading()
                    binding.srlHomeRoot.isRefreshing = false
                }
            })
        }
    }

    override fun onBackPressByFragment() {
        exitScreen()
    }

    override fun onDestroy() {
        removeListener()
        super.onDestroy()
    }

    private fun addHeader() {
        binding.srlHomeRoot.setColorSchemeResources(R.color.primary)
    }

    private fun addAdapter() {
        binding.cvHomeRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(homeAdapter)
        }

        binding.srlHomeRoot.setOnRefreshListener {
            if (AppPreferences.userInfo?.role == ROLE_TYPE.STUDENT_TYPE) {
                viewModel.getHomeStudent(true)
            } else {
                viewModel.getHomeTeacher(true)
            }
        }
    }

    private fun addListener() {
        homeAdapter.listenerHome = object : IHomeListener {
            override fun onClickTeachViewMore() {
                replaceFragment(fragment = LessonAllFragment(), screenAnim = SlideAnimation())
            }

            override fun onClickEvaluateViewMore() {
                replaceFragment(fragment = LessonEvaluateFragment(), screenAnim = SlideAnimation())
            }

            override fun onClickClassRegisterViewMore() {
                replaceFragment(fragment = ClassAllFragment(), screenAnim = SlideAnimation())
            }

            override fun onClickClassWaitingConfirm() {
                replaceFragment(fragment = ClassWaitingConfirmFragment(), screenAnim = SlideAnimation())
            }

            override fun onClickConfirmRegisterClass(classId: String) {
                showDialogConfirm(classId)
            }
        }
    }

    private fun showDialogConfirm(classId: String) {
        BottomSheetConfirmDialog().apply {
            avatar = getAppDrawable(R.drawable.ic_class_empty)
            title = getAppString(R.string.accept_class)
            content = getAppString(R.string.accept_class_question)
            textLeft = getAppString(R.string.confirm)
            textRight = getAppString(R.string.cancel)
            bgTextLeft = getAppDrawable(R.drawable.ripple_bg_primary_corner_14)
            bgTextRight = getAppDrawable(R.drawable.ripple_bg_gray_corner_14_stroke_1)
            clTextLeft = getAppColor(R.color.white)
            clTextRight = getAppColor(R.color.neutral_13)

            eventLeftClick {
                viewModel.updateClassRegister(classId)
            }
        }.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }

    private fun removeListener() {
        homeAdapter.listenerHome = null
    }
}
