package lt.markav.adaptivecalendar

import android.os.Build
import org.joda.time.DateTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(Build.VERSION_CODES.KITKAT))
class MonthViewTest {

    lateinit var sut: MonthView
    lateinit var date: DateTime
    lateinit var calendarView: CalendarView

    @Before
    fun setUp() {
        date = DateTime(2016, 8, 15, 0, 0, 0)
        calendarView = CalendarView(RuntimeEnvironment.application)
        sut = MonthView(RuntimeEnvironment.application, date, calendarView)
    }



}