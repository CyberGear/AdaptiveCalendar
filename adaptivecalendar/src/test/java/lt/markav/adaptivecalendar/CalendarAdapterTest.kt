package lt.markav.adaptivecalendar

import android.os.Build
import android.view.View
import android.widget.TextView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.KITKAT))
class CalendarAdapterTest {

    lateinit var sut: CalendarAdapter

    @Before
    fun setUp() {
        sut = CalendarAdapter()
        sut.locale = Locale.ENGLISH
        sut.monthFormat = DateTimeFormat.forPattern("yyyy-MMM")

        val context = RuntimeEnvironment.application
        val calendarView = CalendarView(context, null)
        sut.monthAdapter = CalendarMonthAdapter(context, sut, calendarView)
    }

    @Test
    fun monthLabelMustBeAsExpected() {
        assertEquals("2016-Aug", sut.monthLabel(DateTime(2016, 8, 1, 0, 0, 0)))
    }

    @Test
    fun weekDayLabelMustBeAsExpected() {
        assertEquals("Mon", sut.weekdayLabel(0))
    }

    @Test
    fun viewForDateMustBeMeaningful() {
        val dateTime = DateTime(2016, 8, 1, 0, 0, 0)

        val thisMonth = sut.getViewForDate(dateTime, isThisMonth = true) as TextView
        assertEquals("1", thisMonth.text)
        assertEquals(View.VISIBLE, thisMonth.visibility)

        val otherMonth = sut.getViewForDate(dateTime, isThisMonth = false) as TextView
        assertEquals("1", otherMonth.text)
        assertEquals(View.INVISIBLE, otherMonth.visibility)
    }

    @Test
    fun viewForMonthMustHaveCorrectTitle() {
        val monthLabelView = sut.getMonthLabelView(DateTime(2016, 8, 1, 0, 0, 0)) as TextView
        assertEquals("2016-Aug", monthLabelView.text)
    }

    @Test
    fun viewForWeekDayMustHaveCorrectTitle() {
        val weekDayTitle = sut.getWeekDayLabelView(0) as TextView
        assertEquals("Mon", weekDayTitle.text)
    }
}