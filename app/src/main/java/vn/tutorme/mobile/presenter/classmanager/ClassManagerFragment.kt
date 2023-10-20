package vn.tutorme.mobile.presenter.classmanager

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.ClassManagerFragmentBinding
import vn.tutorme.mobile.domain.model.category.getDataCategoryClassType
import vn.tutorme.mobile.presenter.home.HomeFragment

@AndroidEntryPoint
class ClassManagerFragment : TutorMeFragment<ClassManagerFragmentBinding>(R.layout.class_manager_fragment) {

    private val pageDataReview by lazy { PageDataClassAdapter(parentFragmentManager, lifecycle) }

    override fun onPrepareInitView() {
        super.onPrepareInitView()

        val fragmentList = mutableListOf<Fragment>(
            ClassChildFragment().apply {
                classType = CLASS_TYPE.REGULAR_TYPE
            }, ClassChildFragment().apply {
                classType = CLASS_TYPE.DEMO_TYPE
            })

        pageDataReview.setFragment(fragmentList)
    }

    override fun onInitView() {
        super.onInitView()
        addHeader()
        addAdapter()
    }

    override fun onBackPressByFragment() {
        replaceFragmentInitialState(HomeFragment(), mainViewModel.indexFragmentInBackStack)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vpClassManagerTab.adapter = null
    }

    private fun addAdapter() {
        binding.ccvClassManagerTabParent.setOnclickTabCategory {
            val type = if (it.id == 1) CLASS_TYPE.REGULAR_TYPE else CLASS_TYPE.DEMO_TYPE
            binding.vpClassManagerTab.setCurrentItem(type.value, false)
        }
    }

    private fun addHeader() {
        binding.ccvClassManagerTabParent.addDataList(getDataCategoryClassType())

        binding.vpClassManagerTab.apply {
            adapter = pageDataReview
            currentItem = 0
            offscreenPageLimit = 2

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    binding.ccvClassManagerTabParent.setStatusCategory(position)
                }
            })
            isSaveEnabled = false
        }
    }
}
