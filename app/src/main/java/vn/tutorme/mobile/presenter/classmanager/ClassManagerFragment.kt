package vn.tutorme.mobile.presenter.classmanager

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDimensionPixel
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassManagerFragmentBinding
import vn.tutorme.mobile.domain.model.category.getDataCategoryClassState
import vn.tutorme.mobile.domain.model.category.getDataCategoryClassType
import vn.tutorme.mobile.presenter.widget.categoryclass.CategoryClassAdapter

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
        binding.srlClassManagerReload.setColorSchemeResources(R.color.primary)
        binding.ccvClassManagerTabParent.addDataList(getDataCategoryClassType())
        binding.ccvClassManagerTabChild.apply {
            addDataList(getDataCategoryClassState())
            setBuilderItem(
                CategoryClassAdapter.Builder(
                    textColorSelected = getAppColor(R.color.white),
                    bgSelected = getAppDrawable(R.drawable.ripple_bg_primary_corner_10),
                    bgHide = getAppDrawable(R.drawable.ripple_bg_back2_corner_10),
                    marginStart = getAppDimensionPixel(R.dimen.fbase_corner_6),
                    marginEnd = getAppDimensionPixel(R.dimen.fbase_corner_6)
                )
            )
        }
    }
}
