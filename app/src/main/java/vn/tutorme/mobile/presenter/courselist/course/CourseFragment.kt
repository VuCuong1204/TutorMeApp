package vn.tutorme.mobile.presenter.courselist.course

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.AppPreferences
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.loadImage
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.CourseFragmentBinding
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog
import vn.tutorme.mobile.presenter.dialog.select.SelectClassDialog
import vn.tutorme.mobile.presenter.dialog.select.model.ClassDetailDisplay

@AndroidEntryPoint
class CourseFragment : TutorMeFragment<CourseFragmentBinding>(R.layout.course_fragment) {

    companion object {
        const val COURSE_ID_KEY = "COURSE_ID_KEY"
    }

    private val viewModel by viewModels<CourseViewModel>()

    private var selectClassDialog: SelectClassDialog? = null

    override fun onInitView() {
        super.onInitView()
        addHeader()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.courseInfoState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    it.data?.let { value -> setCourseInfo(value) }
                }
            }, canShowLoading = true)
        }

        coroutinesLaunch(viewModel.checkRegisteredState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    if (it.data == true) {
                        binding.tvCourseRegister.isEnabled = true
                        binding.tvCourseRegister.background = getAppDrawable(R.drawable.ripple_bg_primary_corner_16)
                    } else {
                        binding.tvCourseRegister.isEnabled = false
                        binding.tvCourseRegister.background = getAppDrawable(R.drawable.ripple_bg_gray_corner_16)
                    }
                }
            })
        }

        coroutinesLaunch(viewModel.classInfoState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    val classDisplayList = mutableListOf<ClassDetailDisplay>()
                    it.data?.forEach { value ->
                        classDisplayList.add(
                            ClassDetailDisplay(value, false)
                        )
                    }
                    showSelectClassDialog(classDisplayList)
                }
            }, canShowLoading = true)
        }

        coroutinesLaunch(viewModel.registerCourseState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    showSuccess(getAppString(R.string.register_course_success))
                    binding.tvCourseRegister.isEnabled = false
                    binding.tvCourseRegister.background = getAppDrawable(R.drawable.ripple_bg_gray_corner_16)
                }
            })
        }
    }

    private fun addHeader() {
        binding.ivCourseBack.setOnSafeClick {
            onBackPressByFragment()
        }

        binding.tvCourseRegister.setOnSafeClick {
            viewModel.getClassListFromCourse(viewModel.courseId)
        }
    }

    private fun showSelectClassDialog(classList: List<ClassDetailDisplay>) {
        selectClassDialog = SelectClassDialog().apply {
            classDetailDisplayList = classList
            listener = object : SelectClassDialog.IListener {
                override fun onConfirmClick(item: ClassInfo) {
                    showConfirmRegisterDialog(item)
                }
            }
        }

        selectClassDialog?.show(childFragmentManager, SelectClassDialog::class.java.simpleName)
    }

    fun showConfirmRegisterDialog(item: ClassInfo) {
        val bottomSheetConfirmDialog = BottomSheetConfirmDialog().apply {
            title = getAppString(R.string.confirm_second)
            content = getAppString(R.string.delete_register_course)
            textLeft = getAppString(R.string.confirm)
            avatar = getAppDrawable(R.drawable.ic_bell_read_all)
            textRight = getAppString(R.string.cancel)
            bgTextLeft = getAppDrawable(R.drawable.ripple_bg_primary_corner_16)
            bgTextRight = getAppDrawable(R.drawable.ripple_bg_white_corner_16_stroke_1)
            clTextLeft = getAppColor(R.color.white)
            clTextRight = getAppColor(R.color.text1)
            eventLeftClick {
                if (item.classId != null && item.lessonFirst != null && item.lessonSecond != null) {
                    viewModel.registerCourse(
                        item.classId!!,
                        AppPreferences.userInfo?.userId?:STRING_DEFAULT,
                        item.lessonFirst!!,
                        item.lessonSecond!!
                    )
                    selectClassDialog?.dismiss()
                }
            }
        }

        bottomSheetConfirmDialog.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }

    private fun setCourseInfo(value: CourseInfo) {
        with(binding) {
            ivCourseBanner.loadImage(value.banner)
            tvCourseTitle.text = value.title
            tvCourseRate.text = value.getCountRating()
            tvCourseDescription.text = value.content
            tvContactDuration.text = value.getTimeLesson()
            tvContactStudent.text = value.getMemberRegister()
            tvContactSubject.text = value.subject
            tvContactCreateDate.text = value.getTimeCreate()
            tvContactEndDate.text = value.getTimeEnd()
            tvCourseClass.text = value.getClassCodeInfo()
            tvCoursePrice.text = value.getPriceValue()
            tvCourseDemo.text = value.getCountDemo()
        }
    }
}
