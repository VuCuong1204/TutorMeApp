package vn.tutorme.mobile.presenter.registerclass

import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassWaitingConfirmFragmentBinding
import vn.tutorme.mobile.domain.model.category.getDataCategoryClass
import vn.tutorme.mobile.domain.model.clazz.CLASS_STATUS

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
                }
            }, canShowLoading = true)
        }
    }

    private fun addAdapter() {
        binding.cvClassWaitingConfirmRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.LINEARLAYOUT_VERTICAL)
            setBaseAdapter(waitingConfirmAdapter)
        }

        binding.ccvClassWaitingConfirmHeader.setOnclickTabCategory {
            when (it.id) {
                INDEX_FIRST_POSITION -> {
                    viewModel.getClassInfoList(true, 0)
                    waitingConfirmAdapter.type = CLASS_STATUS.EMPTY_CLASS_STATUS
                }

                INDEX_SECOND_POSITION -> {
                    waitingConfirmAdapter.type = CLASS_STATUS.RECEIVED_STATUS
                    viewModel.getClassInfoList(true, 1)
                }

                INDEX_THIRD_POSITION -> {
                    waitingConfirmAdapter.type = CLASS_STATUS.OUT_OF_DATE_STATUS
                    viewModel.getClassInfoList(true, 3)
                }
            }
        }
    }

    private fun addHeader() {
        binding.ccvClassWaitingConfirmHeader.addDataList(getDataCategoryClass())
        binding.ivClassWaitingConfirmBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }
}
