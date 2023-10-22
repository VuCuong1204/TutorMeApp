package vn.tutorme.mobile.presenter.dialog.datepicker

import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentManager
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.BaseDialog
import vn.tutorme.mobile.base.common.DialogScreen
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.setOnSafeClick
import vn.tutorme.mobile.presenter.widget.datapicker.DatePickerView
import java.util.Calendar
import java.util.Date

class DatePickerDialog(
    var title: String? = null,
    private var datePickerListener: ((Calendar) -> Unit)? = null,
    private var currentCalendar: Calendar? = null,
    var dateType: DATE_TYPE? = null
) : BaseDialog(R.layout.date_picker_dialog) {

    private lateinit var tvDatePickerDlgTitle: AppCompatTextView
    private lateinit var tvDatePickerDlgSave: AppCompatTextView
    private lateinit var dpvDatePickerDlg: DatePickerView

    override fun getBackgroundId(): Int = R.id.constDatePickerDlgRoot

    override fun screen() = DialogScreen().apply {
        mode = DialogScreen.DIALOG_MODE.BOTTOM
        isFullWidth = true
    }

    override fun onInitView() {
        val cal = Calendar.getInstance()
        tvDatePickerDlgTitle = viewRoot.findViewById(R.id.tvDatePickerDlgTitle)
        dpvDatePickerDlg = viewRoot.findViewById(R.id.dpvDatePickerDlg)
        tvDatePickerDlgSave = viewRoot.findViewById(R.id.tvDatePickerDlgSave)

        dpvDatePickerDlg.apply {
            setLocale(DatePickerView.LOCALE.VI)
            currentCalendar?.let {
                setCurrentCalendar(it)
            }
            when (dateType) {
                DATE_TYPE.FEATURE -> {
                    setMinYear(Calendar.getInstance().get(Calendar.YEAR))
                    setMaxYear(Calendar.getInstance().get(Calendar.YEAR) + 100)
                }

                DATE_TYPE.PASS -> {
                    setMinYear(Calendar.getInstance().get(Calendar.YEAR) - 100)
                    setMaxYear(Calendar.getInstance().get(Calendar.YEAR))
                }

                else -> {}
            }
            listener = object : DatePickerView.Callback {
                override fun onDate(date: Date?) {
                    when (dateType) {
                        DATE_TYPE.PASS -> {
                            if (date?.compareTo(cal.time)!! > 0) {
                                setEnableSaveButton(false)
                            } else {
                                setEnableSaveButton(true)
                            }
                        }

                        DATE_TYPE.FEATURE -> {
                            if (date?.compareTo(cal.time)!! < 0) {
                                setEnableSaveButton(false)
                            } else {
                                setEnableSaveButton(true)
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        tvDatePickerDlgTitle.text = title

        tvDatePickerDlgSave.setOnSafeClick {
            dpvDatePickerDlg.apply {
                val calendar = getCalendarCurrent()
                calendar?.let {
                    datePickerListener?.invoke(calendar)
                }
                dismiss()
            }
        }
    }

    private fun setEnableSaveButton(isEnable: Boolean) {
        tvDatePickerDlgSave.isEnabled = isEnable
        tvDatePickerDlgSave.setTextColor(
            if (isEnable) getAppColor(R.color.text_blue) else getAppColor(R.color.text_hint)
        )
    }

    class Builder {
        private var title: String? = null
        private var datePickerListener: ((Calendar) -> Unit)? = null
        private var currentCalendar: Calendar? = null
        private var dateType: DATE_TYPE? = null

        fun setDatePickerListener(listener: (Calendar) -> Unit) = apply {
            this.datePickerListener = listener
        }

        fun setTitle(title: String?) = apply {
            this.title = title
        }

        fun setCurrentCalendar(currentCalendar: Calendar?) = apply {
            this.currentCalendar = currentCalendar
        }

        fun setDateType(dateType: DATE_TYPE?) = apply {
            this.dateType = dateType
        }

        fun show(fragmentManager: FragmentManager?) {
            if (fragmentManager == null) return
            DatePickerDialog(title, datePickerListener, currentCalendar, dateType).show(
                fragmentManager,
                null
            )
        }
    }
}
