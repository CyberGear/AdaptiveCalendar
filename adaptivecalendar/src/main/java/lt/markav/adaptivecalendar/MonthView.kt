package lt.markav.adaptivecalendar

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import org.joda.time.DateTime
import java.util.*
import android.support.v4.view.ViewPager.LayoutParams as ViewPagerParams
import android.view.ViewGroup.LayoutParams as ViewGroupParams

class MonthView(context: Context,
                val dateTime: DateTime,
                val calendarView: CalendarView) : RelativeLayout(context) {

    val columnCount = 6
    val linesCount = 7
    val viewWidth: Int by lazy { (parent as ViewPager).width }
    val viewHeight: Int by lazy { (parent as ViewPager).height }
    val cellWidth: Float by lazy { viewWidth / 7f }
    val cellSize: Int by lazy { cellWidth.toInt() }

    lateinit var progressBar: ProgressBar
    val cells: MutableMap<Coords, View> = HashMap()

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY))
    }

    fun loadBasicCells(adapter: CalendarAdapter) {
        if (calendarView.showMonth) {
            addView(0, -1, adapter.getMonthLabelView(dateTime))
        }
        for (weekDay in 0..columnCount) {
            addView(1, weekDay, adapter.getWeekDayLabelView(weekDay))
        }
        val startFrom = dateTime.minusDays(dateTime.dayOfWeek)
        for (line in 2..linesCount) {
            for (column in 0..columnCount) {
                val daysFromStart = column + 1 + ((line - 2) * 7)
                val date = startFrom.plusDays(daysFromStart)
                val view = addView(line, column, adapter.getViewForDate(date, isThisMonth(date)))
                view.setOnClickListener { calendarView.onDayClickListener?.invoke(date) }
            }
        }
        prepareProgressBar(context)
    }

    private fun isThisMonth(date: DateTime) = dateTime.monthOfYear() == date.monthOfYear()

    private fun addView(line: Int, column: Int, view: View): View {
        view.id = ((line + 1) * 10) + column
        view.layoutParams = createParams(line, column, view.id)

        cells.put(Coords(line, column), view)

        addView(view)
        return view
    }

    private fun createParams(line: Int, column: Int, id: Int): LayoutParams {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.height = cellSize

        if (column != -1) params.width = cellSize
        if (column > 0) params.addRule(RIGHT_OF, id - 1)
        if (line == 1) params.addRule(BELOW, 9)
        if (line > 1) params.addRule(BELOW, id - 10)

        return params
    }

    private fun prepareProgressBar(context: Context) {
        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(CENTER_IN_PARENT, TRUE)
        progressBar.layoutParams = layoutParams
        progressBar.isIndeterminate = true
        addView(progressBar)
    }

    fun updateCells(adapter: CalendarAdapter) {
        removeView(progressBar)

        val nextView = getViewsFrom(0)

        if (calendarView.showMonth) {
            adapter.updateMonthView(nextView(), dateTime)
        }
        for (weekDay in 0..columnCount) {
            adapter.updateWeekDayView(nextView(), weekDay)
        }
        val startFrom = dateTime.minusDays(dateTime.dayOfWeek)
        for (line in 2..linesCount) {
            for (column in 0..columnCount) {
                val daysFromStart = column + 1 + ((line - 2) * 7)
                val date = startFrom.plusDays(daysFromStart)
                adapter.updateDayView(nextView(), date, isThisMonth(date))
            }
        }
    }

    private fun getViewsFrom(startIndex: Int): () -> View {
        var index = startIndex
        return { getChildAt(index++) }
    }

    override fun toString(): String = "MonthView(${dateTime.toString("yyyy-MM")})"

    data class Coords(val x: Int, val y: Int)

}
