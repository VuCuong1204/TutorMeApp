package vn.tutorme.mobile.presenter.courselist

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.syntheticapp.presenter.widget.collection.LAYOUT_MANAGER
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.common.anim.SlideAnimation
import vn.tutorme.mobile.base.extension.coroutinesLaunch
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeFragment
import vn.tutorme.mobile.databinding.CourseListFragmentBinding
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.presenter.courselist.course.CourseFragment

@AndroidEntryPoint
class CourseListFragment : TutorMeFragment<CourseListFragmentBinding>(R.layout.course_list_fragment) {

    private val viewModel by viewModels<CourseListViewModel>()
    private val courseListAdapter by lazy { CourseListAdapter() }

    override fun onInitView() {
        super.onInitView()
        setAdapter()
        binding.srlCourseListReload.setColorSchemeResources(R.color.primary)
        binding.ivCourseListBack.setOnSafeClick {
            onBackPressByFragment()
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        coroutinesLaunch(viewModel.courseListState) {
            handleUiState(it, object : IViewListener {

                override fun onFailure() {
                    binding.srlCourseListReload.isRefreshing = false
                    super.onFailure()
                }

                override fun onSuccess() {
                    binding.cvCourseListRoot.submitList(it.data)
                    binding.srlCourseListReload.isRefreshing = false
                }
            })
        }
    }

    override fun onDestroyView() {
        removeListener()
        super.onDestroyView()
    }

    private fun setAdapter() {
        binding.cvCourseListRoot.apply {
            setBaseLayoutManager(LAYOUT_MANAGER.GRIDLAYOUT_VERTICAL)
            setBaseAdapter(courseListAdapter)
        }

        binding.srlCourseListReload.setOnRefreshListener {
            viewModel.getCourseList(false)
        }

        addListener()
    }

    private fun addListener() {
        courseListAdapter.listener = object : ICourseListListener {
            override fun onItemClick(item: CourseInfo) {
                replaceFragment(
                    fragment = CourseFragment(),
                    bundle = bundleOf(CourseFragment.COURSE_ID_KEY to item.courseId),
                    screenAnim = SlideAnimation()
                )
            }
        }
    }

    private fun removeListener() {
        courseListAdapter.listener = null
    }
}
