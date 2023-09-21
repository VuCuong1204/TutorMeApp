package vn.tutorme.mobile.presenter.home

import androidx.lifecycle.SavedStateHandle
import vn.tutorme.mobile.base.common.BaseViewModel

class HomeViewModel(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

    val id = savedStateHandle.get<String>(HomeFragment.USER_ID_KEY)
}
