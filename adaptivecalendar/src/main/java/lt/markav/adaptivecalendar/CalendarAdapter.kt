package lt.markav.adaptivecalendar

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

open class CalendarAdapter {

    private val monday: DateTime by lazy {
        val now = DateTime.now()
        now.minusDays(now.dayOfWeek - 1)
    }
    lateinit var monthAdapter: CalendarMonthAdapter
    var isLocaleChanged = false
    var weekdayFormat: DateTimeFormatter = DateTimeFormat.forPattern("EEE")
    var monthFormat: DateTimeFormatter = DateTimeFormat.forPattern("MMMM")
    var locale: Locale = Locale.getDefault()
        set(value) {
            isLocaleChanged = true
            field = value
        }

    open fun loadDataForMonth(month: DateTime): Unit {
        dataLoadedForMonth(month, null)
    }

    open fun getMonthLabelView(dateTime: DateTime): View {
        return createTextView(Typeface.BOLD, monthLabel(dateTime)) {
            it.setTextAppearance(monthAdapter.context, R.style.TextAppearance_AppCompat_Subhead)
        }
    }

    open fun getWeekDayLabelView(weekDay: Int): View {
        return createTextView(Typeface.BOLD, weekdayLabel(weekDay)) {
            it.setTextAppearance(monthAdapter.context, R.style.TextAppearance_AppCompat_Body1)
        }
    }

    open fun getViewForDate(dateTime: DateTime, isThisMonth: Boolean): View {
        return createTextView(Typeface.NORMAL, dateTime.toString("d")) {
            if (isThisMonth) {
                it.isClickable = true
                it.setBackgroundResource(R.drawable.round_bg)
                it.setTextColor(it.resources.getColorStateList(R.color.text_color))
            }
            it.visibility = if (isThisMonth) View.VISIBLE else View.INVISIBLE
        }
    }

    fun dataLoadedForMonth(month: DateTime, data: Any?) {
        monthAdapter.monthLoaded(month, data)
    }

    open fun updateMonthView(view: View, month: DateTime, data: Any?) {
    }

    open fun updateWeekDayView(view: View, weekDay: Int, data: Any?) {
    }

    open fun updateDayView(view: View, date: DateTime, thisMonth: Boolean, data: Any?) {
    }

    inline private fun createTextView(style: Int, text: String, f: (tv: TextView) -> Unit): TextView {
        val textView = TextView(monthAdapter.context)
        textView.gravity = Gravity.CENTER
        textView.text = text
        f(textView)
        textView.setTypeface(textView.typeface, style)
        return textView
    }

    fun String.camel(): String {
        return this[0].toUpperCase() + this.substring(1)
    }

    fun weekdayLabel(weekDay: Int): String {
        return weekdayFormat.withLocale(locale).print(monday.plusDays(weekDay)).camel()
    }

    fun monthLabel(date: DateTime): String {
        return monthFormat.withLocale(locale).print(date).camel()
    }

}