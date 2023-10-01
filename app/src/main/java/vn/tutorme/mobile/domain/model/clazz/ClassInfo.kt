package vn.tutorme.mobile.domain.model.clazz

import kotlinx.parcelize.Parcelize
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppString
import vn.tutorme.mobile.base.model.IParcelable

@Parcelize
data class ClassInfo(
    var classId: String? = null,
    var timeBeginLearn: String? = null,
    var level: String? = null,
    var classStatus: CLASS_STATUS,
    var titleClass: String? = null,
    var totalNumber: Int? = null
) : IParcelable {
    fun getTimeBegin(): String {
        return "Khai giảng 21.12.2023"
    }

    fun getNumberMember(): String {
        return String.format(getAppString(R.string.number_member), totalNumber)
    }
}

fun mockDataClassInfo(size: Int = 20): List<ClassInfo> {

    val list = mutableListOf<ClassInfo>()

    repeat(size) {
        list.add(ClassInfo(
            classId = "Mã lớp D5C.045137",
            level = "Nâng cao",
            totalNumber = 20,
            classStatus = CLASS_STATUS.EMPTY_CLASS_STATUS,
            titleClass = "Lớp 2"
        ))
    }

    return list
}
