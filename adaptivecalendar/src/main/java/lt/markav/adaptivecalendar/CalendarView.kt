package lt.markav.adaptivecalendar

import android.content.Context
import android.support.v4.view.PagerTabStrip
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity

class CalendarView(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    constructor(context: Context) : this(context, null)

    var calendarAdapter: CalendarAdapter? = null

    init {
        layoutParams = LayoutParams()
        setAdapter(CalendarAdapter())
    }

    fun setAdapter(calendarAdapter: CalendarAdapter) {
        this.calendarAdapter = calendarAdapter
        post {
            adapter = CalendarMonthAdapter(context, calendarAdapter)
            currentItem = (adapter as CalendarMonthAdapter).middlePage
        }
    }

    class LayoutParams() : android.view.ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

    fun PagerTabStrip.top(): PagerTabStrip {
        this.setGravity(Gravity.TOP)
        return this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth * 8 / 7)
    }

}
