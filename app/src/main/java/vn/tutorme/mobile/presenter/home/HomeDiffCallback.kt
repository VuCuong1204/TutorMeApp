package vn.tutorme.mobile.presenter.home

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback
import vn.tutorme.mobile.base.extension.Extension.STRING_DEFAULT
import vn.tutorme.mobile.domain.model.clazz.ClassInfo
import vn.tutorme.mobile.domain.model.course.CourseInfo
import vn.tutorme.mobile.domain.model.mission.MissionInfo

class HomeDiffCallback(
    oldList: List<Any>,
    newList: List<Any>
) : BaseDiffUtilCallback<Any>(oldList, newList) {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition)
        val newItem = getNewItem(newItemPosition)

        return if (oldItem is List<*> && newItem is List<*>) {
            if (oldItem[0] is ClassInfo && newItem[0] is ClassInfo) {
                (newItem[0] as ClassInfo).classId == STRING_DEFAULT
            } else {
                oldItem.count() == newItem.count()
            }
        } else if (oldItem is MissionInfo && newItem is MissionInfo) {
            oldItem.hashCode() == newItem.hashCode()
        } else if (oldItem is CourseInfo && newItem is CourseInfo) {
            oldItem.courseId == newItem.courseId
        } else if (oldItem is TITLE_HOME_TYPE && newItem is TITLE_HOME_TYPE) {
            oldItem.value == newItem.value
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getOldItem(oldItemPosition)
        val newItem = getNewItem(newItemPosition)

        return if (oldItem is List<*> && newItem is List<*>) {
            if (oldItem[0] is ClassInfo && newItem[0] is ClassInfo) {
                (newItem[0] as ClassInfo).classId == STRING_DEFAULT
            } else {
                oldItem.count() == newItem.count()
            }
        } else if (oldItem is MissionInfo && newItem is MissionInfo) {
            oldItem.hashCode() == newItem.hashCode()
        } else if (oldItem is CourseInfo && newItem is CourseInfo) {
            oldItem.hashCode() == newItem.hashCode()
        } else if (oldItem is TITLE_HOME_TYPE && newItem is TITLE_HOME_TYPE) {
            oldItem.value == newItem.value
        } else {
            false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val list = mutableListOf<Any>()
        val oldItem = getOldItem(oldItemPosition)
        val newItem = getNewItem(newItemPosition)

        if (oldItem is List<*> && newItem is List<*>) {
            if (oldItem[0] is ClassInfo && newItem[0] is ClassInfo) {
                oldItem.count() != newItem.count()
            }
        }

        return list.ifEmpty { null }
    }
}

const val CLASS_TEACHER_PAYLOAD = "CLASS_TEACHER_PAYLOAD"
