package lt.markav.adaptivecalendar

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import org.joda.time.DateTime
import org.joda.time.Months
import java.util.*

class CalendarMonthAdapter(val context: Context,
                           val adapter: CalendarAdapter,
                           val calendarView: CalendarView) : PagerAdapter() {

    val totalPages: Int
    val middlePage: Int
    val initialDate: DateTime

    val monthPages: MutableMap<Month, MonthView> = HashMap()

    init {
        val firstMonth = adapter.getFirstMonth().firstOfMonth()
        val lastMonth = adapter.getLastMonth().firstOfMonth().plusMonths(1)

        initialDate = adapter.getInitialMonth().firstOfMonth()
        totalPages = Months.monthsBetween(firstMonth, lastMonth).months
        middlePage = calcMiddlePage(firstMonth, lastMonth)

        adapter.monthAdapter = this
    }

    private fun calcMiddlePage(firstMonth: DateTime, lastMonth: DateTime): Int {
        val monthsToFirst = Months.monthsBetween(firstMonth, initialDate).months
        val monthsToLast = Months.monthsBetween(initialDate, lastMonth).months

        if (monthsToFirst > totalPages || monthsToLast > totalPages) {
            throw CalendarException("Initial Month [${initialDate.printMonth()}] " +
                    "is not in declared range " +
                    "[${firstMonth.printMonth()} - ${lastMonth.printMonth()}]")
        }

        return totalPages - monthsToLast
    }

    override fun isViewFromObject(view: View?, any: Any?): Boolean {
        return view != null && any != null && view.equals(any)
    }

    override fun getCount(): Int = totalPages

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val dateTime = getShiftedDate(position)
        val month = Month(dateTime)
        val view = MonthView(context, dateTime, calendarView)
        container.addView(view)

        monthPages.put(month, view)

        view.loadBasicCells(adapter)
        adapter.loadDataForMonth(dateTime)
        return view
    }

    fun refresh() {
        monthPages.keys.forEach { adapter.loadDataForMonth(it.toDateTime()) }
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        monthPages.remove(Month(getShiftedDate(position)))
        container.removeView(any as View)
    }

    fun monthLoaded(month: DateTime, data: Any?) {
        monthPages[Month(month)]?.updateCells(adapter, data)
    }

    fun getShiftedDate(position: Int): DateTime {
        val offset = position - middlePage
        return initialDate.plusMonths(offset)
    }

    fun getShift(dateTime: DateTime): Int {
        val firstOfMonth = dateTime.firstOfMonth()
        val monthsBetween = Months.monthsBetween(initialDate, firstOfMonth).months
        return middlePage + monthsBetween
    }

    override fun getPageTitle(position: Int): CharSequence {
        return adapter.monthLabel(getShiftedDate(position))
    }

    data class Month(val year: Int, val month: Int) {
        constructor(dateTime: DateTime) : this(dateTime.year, dateTime.monthOfYear)

        fun toDateTime() = DateTime(year, month, 1, 0, 0)
    }

}
