package vn.tutorme.mobile.presenter.classmanager

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassManagerFragmentBinding
import vn.tutorme.mobile.domain.model.category.getDataCategoryClassType

@AndroidEntryPoint
class ClassManagerFragment : TutorMeFragment<ClassManagerFragmentBinding>(R.layout.class_manager_fragment) {

    private val viewModel by viewModels<ClassManagerViewModel>()

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
    }

    private fun addAdapter() {
        binding.ccvClassManagerTabParent.setOnclickTabCategory {
            when (it.type) {
                CLASS_TYPE.REGULAR_TYPE -> {}
                CLASS_TYPE.DEMO_TYPE -> {}
            }
        }
    }

    private fun addHeader() {
        binding.ccvClassManagerTabParent.addDataList(getDataCategoryClassType())
    }
}
