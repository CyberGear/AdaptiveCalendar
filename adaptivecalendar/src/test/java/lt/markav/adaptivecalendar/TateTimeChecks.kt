package lt.markav.adaptivecalendar

import org.joda.time.DateTime
import org.joda.time.Months
import org.junit.Test
import kotlin.test.assertEquals

class TateTimeChecks {

    @Test
    @Throws(Exception::class)
    fun testMonthBetween() {
        val now = DateTime.now()
        val from = DateTime(now.year, now.monthOfYear - 2, 1, 0, 0)
        val to = DateTime(now.year, now.monthOfYear, 1, 0, 0)

        assertEquals(2, Months.monthsBetween(from, to).months)
    }
}
