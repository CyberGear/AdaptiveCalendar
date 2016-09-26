package lt.markav.adaptivecalendar

import android.content.Context
import android.support.v4.view.PagerTabStrip
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import org.joda.time.DateTime
import java.util.*

open class CalendarView(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    constructor(context: Context) : this(context, null)

    var calendarAdapter: CalendarAdapter? = null

    var showMonthTitle: Boolean = true
    var showDayTitles: Boolean = true
    var locale: Locale = Locale.getDefault()
        set(value) {
            calendarAdapter?.locale = locale
            field = value
        }

    var onMonthSelectListener: ((dateTime: DateTime) -> Unit)? = null
    var onDayClickListener: ((view:View, dateTime: DateTime) -> Unit)? = null
    var onPageScrollStateChangListener: ((state: Int) -> Unit)? = null
    var onPageScrollListener: ((position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit)? = null
    var onPageSelectListener: ((position: Int) -> Unit)? = null

    fun getCurrentMonth(): DateTime = getMonthAdapter().getShiftedDate(currentItem)

    fun setCurrentMonth(dateTime: DateTime) = setCurrentMonth(dateTime, true)

    fun setCurrentMonth(dateTime: DateTime, animate: Boolean) {
        setCurrentItem(getMonthAdapter().getShift(dateTime), animate)
    }

    private fun getMonthAdapter() = adapter as CalendarMonthAdapter

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, 0, 0)
        showMonthTitle = arr.getBoolean(R.styleable.CalendarView_cv_showMonthTitle, true)
        showDayTitles = arr.getBoolean(R.styleable.CalendarView_cv_showDayTitles, true)
        val loc = arr.getString(R.styleable.CalendarView_cv_locale) ?: locale.language
        locale = Locale(loc)
        arr.recycle()

        layoutParams = LayoutParams()
        setAdapter(CalendarAdapter())

        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                onPageScrollStateChangListener?.invoke(state)
            }

            override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
                onPageScrollListener?.invoke(position, offset, offsetPixels)
            }

            override fun onPageSelected(position: Int) {
                onPageSelectListener?.invoke(position)
                onMonthSelectListener?.invoke(getMonthAdapter().getShiftedDate(position))
            }
        })
    }

    fun setAdapter(calendarAdapter: CalendarAdapter) {
        if (!calendarAdapter.isLocaleChanged) {
            calendarAdapter.locale = locale
        }
        this.calendarAdapter = calendarAdapter
        post {
            adapter = CalendarMonthAdapter(context, calendarAdapter, this)
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
        val rows = 6 + (if (showMonthTitle) 1 else 0) + (if (showDayTitles) 1 else 0)
        setMeasuredDimension(measuredWidth, measuredWidth * rows / 7)
    }

    @Suppress("unused")
    fun onMonthSelected(function: (dateTime: DateTime) -> Unit) {
        onMonthSelectListener = function
    }

    @Suppress("unused")
    fun onDayClicked(function: (view:View, dateTime: DateTime) -> Unit) {
        onDayClickListener = function
    }

    @Suppress("unused")
    fun onPageScrollStateChanged(function: (state: Int) -> Unit) {
        onPageScrollStateChangListener = function
    }

    @Suppress("unused")
    fun onPageScrolled(function: (position: Int, offset: Float, offsetPixels: Int) -> Unit) {
        onPageScrollListener = function
    }

    @Suppress("unused")
    fun onPageSelected(function: (position: Int) -> Unit) {
        onPageSelectListener = function
    }

}
