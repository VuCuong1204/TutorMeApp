package vn.tutorme.mobile.presenter.widget.datapicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import vn.tutorme.mobile.R
import vn.tutorme.mobile.base.extension.getAppColor
import vn.tutorme.mobile.base.extension.getAppDimension
import vn.tutorme.mobile.base.extension.gone
import vn.tutorme.mobile.base.extension.show
import java.lang.reflect.Field
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DatePickerView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(ctx, attrs, defStyle) {
    companion object {
        private val TAG = DatePickerView::class.java.simpleName
        private const val DAY_OFFSET = 1
        private const val YEAR_OFFSET = 50
        private const val FORMAT_YYYY_MM_DD = "yyyy/MM/dd"
        private const val LOCALE_INVALID = -1
    }

    private var numberValueListener: NumberValueChangeListener? = null
    private var pickerYear: NumberPicker? = null
    private var pickerMonth: NumberPicker? = null
    private var pickerDay: NumberPicker? = null
    private var columnWidth: Int = getAppDimension(R.dimen.fbase_dimen_70).toInt()
    private var columnSpace: Int = 0
    private var currCalendar: Calendar = Calendar.getInstance()
    private var currYear: Int = 0
    private var currMonth: Int = 0
    private var currDay: Int = 0
    private var currDaysInMonth: Int = 0
    private var monthNameList = arrayOf<String>()
    private var locale: LOCALE? = null

    var modeFormat: MODE_FORMAT = MODE_FORMAT.DAY_MONTH_YEAR
        private set
    var listener: Callback? = null

    init {
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.date_picker_layout, this)
        init(attrs)
        initView(view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (modeFormat.value) {
            MODE_FORMAT.DAY_MONTH_YEAR.value -> {
                pickerDay?.show()
                pickerMonth?.show()
                pickerYear?.show()
            }

            MODE_FORMAT.MONTH_YEAR.value -> {
                pickerDay?.gone()
                pickerMonth?.show()
                pickerYear?.show()
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun changeMode(mode: MODE_FORMAT) {
        modeFormat = mode
        requestLayout()
    }

    fun setLocale(locale: LOCALE) {
        monthNameList = getMonthNameList(locale)
        requestLayout()
    }

    fun setMaxDay(maxDay: Int) {
        pickerDay?.maxValue = maxDay
        requestLayout()
    }

    fun setMinDay(minDay: Int) {
        pickerDay?.minValue = minDay
        requestLayout()
    }

    fun setMaxMonth(maxMonth: Int) {
        pickerMonth?.maxValue = maxMonth
        requestLayout()
    }

    fun setMinMonth(minMonth: Int) {
        pickerMonth?.minValue = minMonth
        requestLayout()
    }

    fun setMaxYear(maxYear: Int) {
        pickerYear?.maxValue = maxYear
        requestLayout()
    }

    fun setMinYear(minYear: Int) {
        pickerYear?.minValue = minYear
        requestLayout()
    }

    fun setCurrentCalendar(currentCalendar: Calendar) {
        this.currCalendar = currentCalendar
        initialDate()
    }

    fun getDateCurrent(): Date? {
        return getDate(pickerYear?.value!!, pickerMonth?.value!!, pickerDay?.value!!)
    }

    fun getCalendarCurrent(): Calendar? {
        return getSelectedCalendar(pickerYear?.value!!, pickerMonth?.value!!, pickerDay?.value!!)
    }

    @SuppressLint("SoonBlockedPrivateApi")
    fun setEnabledNumberPicker(numberPicker: NumberPicker, isEnable: Boolean) {
        numberPicker.isEnabled = isEnable
        val enablePickerColor = getAppColor(R.color.text_hint)
        val disablePickerColor = getAppColor(R.color.text_blue)

        try {
            val selectorWheelPaintField: Field = numberPicker.javaClass.getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            if (isEnable)
                (selectorWheelPaintField.get(numberPicker) as Paint).color = enablePickerColor
            else
                (selectorWheelPaintField.get(numberPicker) as Paint).color = disablePickerColor
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        val count = numberPicker.childCount
        for (i in 0 until count) {
            val child = numberPicker.getChildAt(i)
            if (child is EditText) {
                if (isEnable)
                    child.setTextColor(enablePickerColor)
                else
                    child.setTextColor(disablePickerColor)

            }
        }
        numberPicker.invalidate()
    }

    private fun getMonthNameList(locale: LOCALE?): Array<String> {
        return DateFormatSymbols.getInstance(
            if (locale != null) {
                Locale(locale.value)
            } else {
                Locale.getDefault()
            }
        ).months
    }

    private fun initView(view: View) {
        pickerDay = view.findViewById(R.id.npDay)
        pickerMonth = view.findViewById(R.id.npMonth)
        pickerYear = view.findViewById(R.id.npYear)

        initialDate()
        numberValueListener = NumberValueChangeListener()
        initYearPicker()
        initMonthPicker()
        initDayPicker()
    }

    private fun initialDate() {
        loadCurrentTime()

        pickerYear?.apply {
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            minValue = currYear - YEAR_OFFSET
            maxValue = currYear + YEAR_OFFSET
            value = currYear
        }

        pickerMonth?.apply {
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            displayedValues = monthNameList
            minValue = 0
            maxValue = 11
            value = currMonth
        }

        pickerDay?.apply {
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            minValue = 1
            maxValue = currDaysInMonth
            value = currDay
        }

//        setEnabledNumberPicker(pickerYear!!, true)
    }

    private fun loadCurrentTime() {
        this.currDay = currCalendar.get(Calendar.DAY_OF_MONTH)
        this.currMonth = currCalendar.get(Calendar.MONTH)
        this.currYear = currCalendar.get(Calendar.YEAR)
        this.currDaysInMonth = currCalendar.getActualMaximum(Calendar.DATE)
    }

    private fun initYearPicker() {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(columnSpace, 0, columnSpace, 0)

        pickerYear?.apply {
            layoutParams = params
            layoutParams?.width = columnWidth
            setOnValueChangedListener(numberValueListener)
        }
    }

    private fun initMonthPicker() {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(columnSpace, 0, columnSpace, 0)

        pickerMonth?.apply {
            layoutParams = params
            layoutParams?.apply {
                width = columnWidth
            }
            setOnValueChangedListener(numberValueListener)
        }
    }

    private fun initDayPicker() {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(columnSpace, 0, columnSpace, 0)

        pickerDay?.apply {
            layoutParams = params
            layoutParams?.width = columnWidth
            setOnValueChangedListener(numberValueListener)
        }
    }

    private fun getDate(year: Int, month: Int, day: Int): Date? {
        val yyyyMMdd = "${year}/${month + DAY_OFFSET}/${day}"

        // gán năm-tháng-ngày hiện tại
        currCalendar.set(year, month, day)
        // cập nhật lại thời gian hiện tại
        loadCurrentTime()

        return try {
            val dateTime = SimpleDateFormat(FORMAT_YYYY_MM_DD).parse(yyyyMMdd)
            Log.d(
                TAG,
                "getDate: yyyy/MM/dd[computer->person->date(obj)]:   [$year/$month/$day]  ->  [$yyyyMMdd]  ->  [$dateTime]"
            )
            dateTime
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Xảy ra lỗi khi parse string to date object")
            null
        }
    }

    private fun getSelectedCalendar(year: Int, month: Int, day: Int): Calendar? {
//        val yyyyMMdd = "${year}/${month + DAY_OFFSET}/${day}"

        // gán năm-tháng-ngày hiện tại
        currCalendar.set(year, month, day)
        // cập nhật lại thời gian hiện tại
        return currCalendar
//        loadCurrentTime()
//
//        return try {
//            val dateTime = SimpleDateFormat(FORMAT_YYYY_MM_DD).parse(yyyyMMdd)
//            Log.d(
//                TAG,
//                "getDate: yyyy/MM/dd[computer->person->date(obj)]:   [$year/$month/$day]  ->  [$yyyyMMdd]  ->  [$dateTime]"
//            )
//            dateTime
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e(TAG, "Xảy ra lỗi khi parse string to date object")
//            null
//        }
    }

    private fun init(attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.DatePickerAdvance, 0, 0)

        // chọn type hiển thị DatePicker
        val modeValue = ta.getInt(
            R.styleable.DatePickerAdvance_dp_mode,
            MODE_FORMAT.DAY_MONTH_YEAR.value
        )
        modeFormat = MODE_FORMAT.valueOfName(modeValue)


        // set width của từng item ngày, tháng, năm
        columnWidth = ta.getDimension(
            R.styleable.DatePickerAdvance_dp_column_width,
            getAppDimension(R.dimen.fbase_dimen_70)
        ).toInt()

        // Chọn ngôn ngữ hiển thị của tháng
        val localeValue = ta.getInt(
            R.styleable.DatePickerAdvance_dp_locale, LOCALE_INVALID
        )
        if (localeValue == LOCALE_INVALID) {
            this.locale = null
        } else {
            this.locale = LOCALE.valueOfName(localeValue)
        }

        monthNameList = getMonthNameList(locale)

        // Set khoảng cách của các item ngày, tháng, năm
        columnSpace = ta.getDimension(
            R.styleable.DatePickerAdvance_dp_column_space,
            0f
        ).toInt()

        ta.recycle()
    }

    interface Callback {

        fun onDate(date: Date?)
    }

    enum class MODE_FORMAT(val value: Int) {
        DAY_MONTH_YEAR(1),
        MONTH_YEAR(2);

        companion object {

            fun valueOfName(value: Int): MODE_FORMAT {
                return when (value) {
                    2 -> MONTH_YEAR
                    else -> DAY_MONTH_YEAR
                }
            }
        }
    }

    enum class LOCALE(val value: String) {
        EN("en"),
        VI("vi");

        companion object {

            fun valueOfName(value: Int): LOCALE {
                return when (value) {
                    1 -> VI
                    else -> EN
                }
            }
        }
    }

    inner class NumberValueChangeListener : NumberPicker.OnValueChangeListener {

        override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
            var year = 0
            var month = 0
            var day = 0

            val calendar: Calendar
            val daysInMonth: Int

            // thời gian tại thời điểm hiện tại
            val presentCalendar = Calendar.getInstance()
            val presentYear = presentCalendar.get(Calendar.YEAR)
            val presentMonth = presentCalendar.get(Calendar.MONTH)
            val presentDay = presentCalendar.get(Calendar.DAY_OF_MONTH)

            when (picker?.id) {
                R.id.npYear -> {
                    // cập nhật lại giá trị của tháng tương ứng
                    numberValueListener?.onValueChange(
                        pickerMonth,
                        pickerMonth?.value!!,
                        pickerMonth?.value!!
                    )
                }

                R.id.npMonth -> {
                    // lấy giá trị hiện
                    year = pickerYear?.value!!
                    month = newVal
                    day = pickerDay?.value!!

                    /*
                    * gán lại `ngày` là mùng 1 khi thay đổi `tháng` chỉ nhắm mục đích lấy ra
                    * `tổng số ngày` trong `tháng` đó, chứ ko có ý nghĩa gán lại `ngày`, nên ko
                    * được cùng `calendar` để lấy ra `ngày` và sử dụng
                    * */
                    calendar = Calendar.getInstance()
                    calendar.set(year, month, 1)

                    // get số ngày max trong năm-tháng hiện tại
                    daysInMonth = calendar.getActualMaximum(Calendar.DATE)
//                    Log.d(TAG, "npMonth:    month: $month, daysInMonth: $daysInMonth")

                    // gán tổng số ngày trong tháng
                    pickerDay?.maxValue = daysInMonth

                    if (year != presentYear && month != presentMonth) {
                        /*
                        * `năm-tháng` vừa được chọn ko phải `năm-tháng` hiện tại thì phải gán
                        * lại giá trị của `ngày` nếu như `ngày` đó lớn hơn `tổng số ngày` trong
                        * `tháng` vừa được chọn
                        * */
                        if (day > daysInMonth) {
                            day = daysInMonth
                        }
                        pickerDay?.value = day
                    }

                    listener?.onDate(getDate(year, month, day))
                }

                R.id.npDay -> {
                    // lấy giá trị hiện
                    year = pickerYear?.value!!
                    month = pickerMonth?.value!!
                    day = newVal

                    listener?.onDate(getDate(year, month, day))
                }
            }
        }
    }
}
