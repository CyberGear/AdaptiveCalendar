package lt.markav.adaptivecalendar

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

open class CalendarAdapter {

    lateinit var monthAdapter: CalendarMonthAdapter
    val monday by lazy {
        val now = DateTime.now()
        now.minusDays(now.dayOfWeek - 1)
    }

    var weekdayFormat = DateTimeFormat.forPattern("EEE")
    var monthFormat = DateTimeFormat.forPattern("MMMM")
    var locale = Locale.getDefault()

    fun loadDataForMonth(month: DateTime): Unit {
        monthLoaded(month)
    }

    fun monthLoaded(month: DateTime) {
        monthAdapter.monthLoaded(month)
    }

    open fun getMonthLabelView(dateTime: DateTime): View {
        val textView = createTextView(Typeface.BOLD, monthLabel(dateTime)) {
            it.setTextAppearance(monthAdapter.context, R.style.TextAppearance_AppCompat_Subhead)
        }
        return textView
    }

    open fun getWeekDayLabelView(weekDay: Int): View {
        val textView = createTextView(Typeface.BOLD, weekdayLabel(weekDay)) {
            it.setTextAppearance(monthAdapter.context, R.style.TextAppearance_AppCompat_Body1)
        }
        return textView
    }

    open fun getViewForDate(dateTime: DateTime, isThisMonth: Boolean): View {
        val textView = createTextView(Typeface.NORMAL, dateTime.toString("d")) {
            if (!isThisMonth) it.setTextColor(0x00FFFFFF.toInt())
        }
        return textView
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