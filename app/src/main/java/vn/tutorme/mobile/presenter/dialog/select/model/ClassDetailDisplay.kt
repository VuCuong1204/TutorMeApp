package vn.tutorme.mobile.presenter.dialog.select.model

import vn.tutorme.mobile.domain.model.clazz.ClassInfo

data class ClassDetailDisplay(
    var classInfo: ClassInfo? = null,
    var isSelected: Boolean? = null
)
