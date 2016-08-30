package lt.markav.calendartest.main

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import lt.markav.adaptivecalendar.CalendarAdapter
import lt.markav.calendartest.R
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar.setAdapter(CA())
    }

}

class CA : CalendarAdapter() {

    init {
        monthFormat = DateTimeFormat.forPattern("MMMM")
        locale = Locale("LT")
    }

    override fun getMonthLabelView(dateTime: DateTime): View {
        val view = super.getMonthLabelView(dateTime)
        (view as TextView).append(" mėn.")
        return view
    }

    override fun getWeekDayLabelView(weekDay: Int): View {
        val view = super.getWeekDayLabelView(weekDay)
        view.setBackgroundColor((if (weekDay > 4) 0xffffffaa else 0x00ffffff).toInt())
        return view
    }

    override fun getViewForDate(dateTime: DateTime, isThisMonth: Boolean): View {
        val view = super.getViewForDate(dateTime, isThisMonth)
        if (isThisMonth) {
            view.setBackgroundColor((if (dateTime.dayOfWeek > 5) 0xffffffaa else 0x00ffffff).toInt())
        }
        return view
    }

}

