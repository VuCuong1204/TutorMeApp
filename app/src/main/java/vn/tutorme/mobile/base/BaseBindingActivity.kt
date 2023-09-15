package vn.tutorme.mobile.base

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<DB : ViewDataBinding>(layoutId: Int) : BaseActivity(layoutId) {
    private var _binding: DB? = null
    protected val binding: DB
        get() = _binding!!


    override fun attachView() {
        _binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
