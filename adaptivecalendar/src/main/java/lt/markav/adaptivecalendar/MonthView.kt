package lt.markav.adaptivecalendar

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.joda.time.DateTime
import java.util.*
import android.view.ViewGroup.LayoutParams as ViewGroupParams
import android.support.v4.view.ViewPager.LayoutParams as ViewPagerParams

class MonthView(context: Context, val dateTime: DateTime) : RelativeLayout(context) {

    val columnCount = 6
    val linesCount = 7
    val cellWidth: Int by lazy {
        (parent as ViewPager).width / 7
    }

    lateinit var bar: ProgressBar
    val cells: MutableMap<Coord, View> = HashMap<Coord, View>()

    init {
        showProgress(context)
    }

    fun loadBasicCells(adapter: CalendarAdapter) {

        addView(0, -1, adapter.getMonthLabelView(dateTime))

        for (weekDay in 0..columnCount) {
            addView(1, weekDay, adapter.getWeekDayLabelView(weekDay))
        }

        val startFrom = dateTime.minusDays(dateTime.dayOfWeek)
        for (line in 2..linesCount) {
            for (column in 0..columnCount) {
                val daysFromStart = column + 1 + ((line - 2) * 7)
                val date = startFrom.plusDays(daysFromStart)
                addView(line, column, adapter.getViewForDate(date, isThisMonth(date)))
            }
        }
    }

    private fun isThisMonth(date: DateTime) = dateTime.monthOfYear() == date.monthOfYear()

    private fun addView(line: Int, column: Int, view: View) {
        view.id = ((line + 1) * 10) + column
        view.layoutParams = createParams(line, column, view.id)

        cells.put(Coord(line, column), view)
        addView(view)
    }

    private fun createParams(line: Int, column: Int, id: Int): LayoutParams {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.height = cellWidth

        if (column != -1) params.width = cellWidth
        if (column > 0) params.addRule(RIGHT_OF, id - 1)
        if (line == 1) params.addRule(BELOW, 9)
        if (line > 1) params.addRule(BELOW, id - 10)

        return params
    }

    private fun showProgress(context: Context) {
        bar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(CENTER_IN_PARENT, TRUE)
        bar.layoutParams = layoutParams
        bar.isIndeterminate = true
        addView(bar)
    }

    fun loadCells(adapter: CalendarAdapter) {
        removeView(bar)
    }

    override fun toString(): String = "MonthView(${dateTime.toString("yyyy-MM")})"

    data class Coord(val x: Int, val y: Int)

}
