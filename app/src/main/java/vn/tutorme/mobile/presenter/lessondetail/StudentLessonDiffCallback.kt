package vn.tutorme.mobile.presenter.lessondetail

import vn.tutorme.mobile.base.adapter.BaseDiffUtilCallback
import vn.tutorme.mobile.domain.model.authen.UserInfo
import vn.tutorme.mobile.domain.model.lesson.LESSON_STATUS
import vn.tutorme.mobile.presenter.lessondetail.model.LessonTypeDisplay

class StudentLessonDiffCallback(
    oldList: List<Any>,
    newList: List<Any>
) : BaseDiffUtilCallback<Any>(oldList, newList) {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (getOldItem(oldItemPosition) is UserInfo &&
            getNewItem(newItemPosition) is UserInfo
        ) {
            val oldItem = getOldItem(oldItemPosition) as UserInfo
            val newItem = getNewItem(newItemPosition) as UserInfo

            oldItem.userId == newItem.userId
        } else if (getOldItem(oldItemPosition) is LessonTypeDisplay &&
            getNewItem(newItemPosition) is LessonTypeDisplay
        ) {
            val oldItem = getOldItem(oldItemPosition) as LessonTypeDisplay
            val newItem = getNewItem(newItemPosition) as LessonTypeDisplay

            if (oldItem.type == LESSON_STATUS.UPCOMING_STATUS &&
                newItem.type == LESSON_STATUS.UPCOMING_STATUS
            ) {
                oldItem.userInfo?.attendanceState == newItem.userInfo?.attendanceState
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (getOldItem(oldItemPosition) is UserInfo &&
            getNewItem(newItemPosition) is UserInfo
        ) {
            val oldItem = getOldItem(oldItemPosition) as? UserInfo
            val newItem = getNewItem(newItemPosition) as? UserInfo

            oldItem?.attendanceState == newItem?.attendanceState ||
                    oldItem?.evaluateState == newItem?.evaluateState
        } else if (getOldItem(oldItemPosition) is LessonTypeDisplay &&
            getNewItem(newItemPosition) is LessonTypeDisplay
        ) {
            val oldItem = getOldItem(oldItemPosition) as LessonTypeDisplay
            val newItem = getNewItem(newItemPosition) as LessonTypeDisplay

            if (oldItem.type == LESSON_STATUS.UPCOMING_STATUS &&
                newItem.type == LESSON_STATUS.UPCOMING_STATUS
            ) {
                oldItem.userInfo?.attendanceState == newItem.userInfo?.attendanceState
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val list = mutableListOf<Any>()

        if (getOldItem(oldItemPosition) is UserInfo &&
            getNewItem(newItemPosition) is UserInfo
        ) {
            val oldItem = getOldItem(oldItemPosition) as? UserInfo
            val newItem = getNewItem(newItemPosition) as? UserInfo
            if (oldItem?.attendanceState != newItem?.attendanceState ||
                oldItem?.evaluateState != newItem?.evaluateState
            ) {
                list.add(ATTENDANCE_PAYLOAD)
            }
        } else if (getOldItem(oldItemPosition) is LessonTypeDisplay &&
            getNewItem(newItemPosition) is LessonTypeDisplay
        ) {
            val oldItem = getOldItem(oldItemPosition) as LessonTypeDisplay
            val newItem = getNewItem(newItemPosition) as LessonTypeDisplay

            if (oldItem.type == LESSON_STATUS.UPCOMING_STATUS &&
                newItem.type == LESSON_STATUS.UPCOMING_STATUS &&
                oldItem.userInfo?.attendanceState != newItem.userInfo?.attendanceState
            ) {
                list.add(ACTION_ATTENDANCE_PAYLOAD)
            }
        }

        return list.ifEmpty { null }
    }
}

const val ATTENDANCE_PAYLOAD = "ATTENDANCE_PAYLOAD"
const val ACTION_ATTENDANCE_PAYLOAD = "ACTION_ATTENDANCE_PAYLOAD"
