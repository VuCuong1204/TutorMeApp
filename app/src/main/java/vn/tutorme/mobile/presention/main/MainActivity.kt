package vn.tutorme.mobile.presention.main

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.common.IViewListener
import vn.tutorme.mobile.base.extension.handleUiState
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.base.screen.TutorMeActivity
import vn.tutorme.mobile.databinding.MainActivityBinding

@AndroidEntryPoint
class MainActivity : TutorMeActivity<MainActivityBinding>(R.layout.main_activity) {

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onInitView() {
        super.onInitView()
        binding.tvMainName.setOnSafeClick {
            showSuccess("hello")
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.userState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.tvMainName.text = it.data
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.userState1.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        binding.tvMainName1.text = it.data
                    }
                })
            }
        }
    }

    override fun showLoading() {
        //   TODO("Not yet implemented")
    }

    override fun hideLoading() {
        //   TODO("Not yet implemented")
    }
}
