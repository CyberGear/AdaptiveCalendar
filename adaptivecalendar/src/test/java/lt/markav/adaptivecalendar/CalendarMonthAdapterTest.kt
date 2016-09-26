package lt.markav.adaptivecalendar

import android.os.Build
import com.nhaarman.mockito_kotlin.*
import org.joda.time.DateTime
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.KITKAT))
class CalendarMonthAdapterTest {

    lateinit var sut: CalendarMonthAdapter
    lateinit var adapter: CalendarAdapter
    lateinit var calendarView: CalendarView

    @Before
    fun setUp() {
        adapter = CalendarAdapter()
        calendarView = CalendarView(RuntimeEnvironment.application)
        sut = CalendarMonthAdapter(RuntimeEnvironment.application, adapter, calendarView)
    }

    @Test
    fun monthShouldBeEqualByBothConstructors() {
        val first = CalendarMonthAdapter.Month(2016, 8)
        val second = CalendarMonthAdapter.Month(DateTime(2016, 8, 1, 0, 0, 0))
        assertEquals(first, second)
    }

    @Test
    fun pageTitleMustBeAsExpected() {
        assertEquals(DateTime.now().toString("MMMM"), sut.getPageTitle(calendarView.currentItem))
    }

    @Test
    fun pageShiftMustBeSameAsMonthDiff() {
        val shift = -10
        val now = DateTime.now()
        val shiftedDay = DateTime(now.year, now.monthOfYear, 1, 0, 0, 0).plusMonths(shift)

        assertEquals(calendarView.currentItem + shift, sut.getShift(shiftedDay))
    }
}