package vn.tutorme.mobile.presenter.notification

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.adapter.BaseVH
import vn.tutorme.mobile.base.adapter.TutorMeAdapter
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.getAppDrawable
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.databinding.NotificationItemBinding
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_STATE
import vn.tutorme.mobile.domain.model.notification.NOTIFICATION_TYPE
import vn.tutorme.mobile.domain.model.notification.NotificationInfo

class NotificationAdapter : TutorMeAdapter() {

    var listener: INotificationListener? = null

    override fun getLayoutResource(viewType: Int): Int = R.layout.notification_item

    override fun onCreateViewHolder(viewType: Int, binding: ViewDataBinding): BaseVH<*>? {
        return NotificationVH(binding as NotificationItemBinding)
    }

    override fun getLayoutLoading(): Int = R.layout.notification_loading_item

    override fun getLayoutEmpty(): Empty = Empty(layoutResource = R.layout.notification_empty)

    override fun getLayoutLoadMore(): LoadMore = LoadMore(layoutResource = R.layout.load_more_item)


    override fun getDiffUtil(oldList: List<Any>, newList: List<Any>): DiffUtil.Callback {
        return NotificationDiffCallback(oldList, newList)
    }

    inner class NotificationVH(private val binding: NotificationItemBinding) : BaseVH<NotificationInfo>(binding) {

        init {
            binding.root.setOnSafeClick {
                getItem {
                    listener?.onReadClick(it)
                }
            }

            binding.root.setOnLongClickListener {
                getItem {
                    it.id?.let { id -> listener?.onDeleteClick(id) }
                }

                return@setOnLongClickListener true
            }
        }

        override fun onBind(data: NotificationInfo) {
            super.onBind(data)

            binding.tvNotificationTitle.text = data.title
            binding.tvNotificationContent.text = data.content
            binding.tvNotificationTime.text = data.getTime()

            binding.ivNotificationType.setImageResource(when (data.notifyType) {
                NOTIFICATION_TYPE.APPROVE_TYPE -> R.drawable.ic_send
                NOTIFICATION_TYPE.PREPARE_STUDY_TYPE -> R.drawable.ic_clock
                NOTIFICATION_TYPE.SYSTEM_TYPE -> R.drawable.ic_trash
                else -> R.drawable.ic_trash
            })

            if (data.notifyState == NOTIFICATION_STATE.READ_STATE) {
                binding.clNotificationRoot.background = getAppDrawable(R.drawable.shape_bg_comp2_corner_14_stroke_comp)
            } else {
                binding.clNotificationRoot.background = getAppDrawable(R.drawable.shape_bg_white_corner_14)
            }

            val params = binding.clNotificationRoot.layoutParams as ViewGroup.MarginLayoutParams
            if (absoluteAdapterPosition == dataList.lastIndex) {
                params.setMargins(
                    0,
                    0,
                    0,
                    getAppDimension(R.dimen.fbase_dimen_86).toInt()
                )
            }
            binding.clNotificationRoot.layoutParams = params
        }

        override fun onBind(data: NotificationInfo, payloads: List<Any>) {
            super.onBind(data, payloads)
            (payloads.firstOrNull() as? List<*>)?.forEach {
                when (it) {
                    READ_STATE_PAYLOAD -> {
                        if (data.notifyState == NOTIFICATION_STATE.READ_STATE) {
                            binding.clNotificationRoot.background = getAppDrawable(R.drawable.shape_bg_comp2_corner_14_stroke_comp)
                        } else {
                            binding.clNotificationRoot.background = getAppDrawable(R.drawable.shape_bg_white_corner_14)
                        }
                    }

                    POSITION_LAST_STATE_PAYLOAD -> {
                        val params = binding.clNotificationRoot.layoutParams as ViewGroup.MarginLayoutParams
                        params.setMargins(
                            0,
                            0,
                            0,
                            getAppDimension(R.dimen.fbase_dimen_12).toInt()
                        )
                        binding.clNotificationRoot.layoutParams = params
                    }
                }
            }
        }
    }
}
