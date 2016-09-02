package lt.markav.adaptivecalendar

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import org.joda.time.DateTime
import java.util.*

class CalendarMonthAdapter(val context: Context,
                           val adapter: CalendarAdapter,
                           val calendarView: CalendarView) : PagerAdapter() {

    val totalPages = 240
    val middlePage = totalPages / 2
    val initialDate: DateTime = firstDayOfThisMonth()

    val monthPages: MutableMap<Month, MonthView> = HashMap()

    init {
        adapter.monthAdapter = this
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

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        monthPages.remove(Month(getShiftedDate(position)))
        container.removeView(any as View)
    }

    fun monthLoaded(month: DateTime) {
        monthPages[Month(month)]?.updateCells(adapter)
    }

    fun getShiftedDate(position: Int): DateTime {
        val offset = position - middlePage
        return initialDate.plusMonths(offset)
    }

    private fun firstDayOfThisMonth(): DateTime {
        val now = DateTime.now()
        return DateTime(now.year, now.monthOfYear, 1, 0, 0, 0)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return adapter.monthLabel(getShiftedDate(position))
    }

    data class Month(val year: Int, val month: Int) {
        constructor(dateTime: DateTime) : this(dateTime.year, dateTime.monthOfYear)
    }

}
