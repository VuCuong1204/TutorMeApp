package vn.tutorme.mobile.base.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseViewModel : ViewModel()

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application)

class ViewModelFactory(private vararg val params: Any) : ViewModelProvider.NewInstanceFactory()
