package vn.tutorme.mobile.presenter.registerclass

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassWaitingConfirmFragmentBinding
import vn.tutorme.mobile.domain.model.category.getDataCategoryClass
import vn.tutorme.mobile.domain.model.clazz.CLASS_STATUS
import vn.tutorme.mobile.presenter.dialog.BottomSheetConfirmDialog

@AndroidEntryPoint
class ClassWaitingConfirmFragment : TutorMeFragment<ClassWaitingConfirmFragmentBinding>(R.layout.class_waiting_confirm_fragment) {

    companion object {
        private const val INDEX_FIRST_POSITION = 1
        private const val INDEX_SECOND_POSITION = 2
        private const val INDEX_THIRD_POSITION = 3
    }

    private val viewModel by viewModels<ClassWaitingConfirmViewModel>()
    private val waitingConfirmAdapter by lazy { ClassWaitingConfirmAdapter() }

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
        addListener()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.classInfoState) {
            handleUiState(it, object : IViewListener {
                override fun onSuccess() {
                    binding.cvClassWaitingConfirmRoot.apply {
                        setBaseAdapter(waitingConfirmAdapter)
                        submitList(it.data?.dataList)
                    }
                    viewModel.reset()
                }
            }, canShowLoading = true)
        }
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    private fun addListener() {
        waitingConfirmAdapter.listener = object : IListener {
            override fun onConfirmClick(classId: String) {
                showDialogConfirm(classId)
            }

            override fun onReceived(classId: String) {
//                TODO("Not yet implemented")
            }

            override fun onCancel(classId: String) {
//                TODO("Not yet implemented")
            }
        }
    }

    private fun removeListener() {
        waitingConfirmAdapter.listener = null
    }

    private fun showDialogConfirm(classId: String) {
        BottomSheetConfirmDialog().apply {
            avatar = getAppDrawable(R.drawable.ic_class_empty)
            title = getAppString(R.string.accept_class)
            content = if (waitingConfirmAdapter.type != CLASS_STATUS.RECEIVED_STATUS)
                getAppString(R.string.accept_class_question) else getAppString(R.string.reject_class_question)
            textLeft = getAppString(R.string.confirm)
            textRight = getAppString(R.string.cancel)
            bgTextLeft = getAppDrawable(R.drawable.ripple_bg_primary_corner_14)
            bgTextRight = getAppDrawable(R.drawable.ripple_bg_gray_corner_14_stroke_1)
            clTextLeft = getAppColor(R.color.white)
            clTextRight = getAppColor(R.color.neutral_13)

            eventLeftClick {
                if (waitingConfirmAdapter.type == CLASS_STATUS.EMPTY_CLASS_STATUS || waitingConfirmAdapter.type == CLASS_STATUS.OUT_OF_DATE_STATUS) {
                    showSuccess(getAppString(R.string.register_receiver_class))
                } else {
                    showSuccess(getAppString(R.string.cancel_register_receiver_class))
                }
                viewModel.updateClassRegister(true, waitingConfirmAdapter.type.value, classId)
            }
        }.show(childFragmentManager, BottomSheetConfirmDialog::class.java.simpleName)
    }

    private fun addAdapter() {
        binding.cvClassWaitingConfirmRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(waitingConfirmAdapter)
        }

        binding.ccvClassWaitingConfirmHeader.setOnclickTabCategory {
            when (it.id) {
                INDEX_FIRST_POSITION -> {
                    waitingConfirmAdapter.type = CLASS_STATUS.EMPTY_CLASS_STATUS
                }

                INDEX_SECOND_POSITION -> {
                    waitingConfirmAdapter.type = CLASS_STATUS.RECEIVED_STATUS
                }

                INDEX_THIRD_POSITION -> {
                    waitingConfirmAdapter.type = CLASS_STATUS.OUT_OF_DATE_STATUS
                }
            }

            viewModel.getClassInfoList(true, waitingConfirmAdapter.type.value)
        }
    }

    private fun addHeader() {
        binding.ccvClassWaitingConfirmHeader.addDataList(getDataCategoryClass())
        binding.ivClassWaitingConfirmBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }
}
