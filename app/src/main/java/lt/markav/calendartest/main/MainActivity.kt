package lt.markav.calendartest.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import lt.markav.adaptivecalendar.CalendarAdapter
import lt.markav.calendartest.R
import org.joda.time.format.DateTimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar.setAdapter(CA())
        calendar.onMonthSelected {

        }
        calendar.onDayClicked {

        }
    }

}

class CA() : CalendarAdapter() {

    init {
        monthFormat = DateTimeFormat.forPattern("MMMM")
        locale = Locale("LT")
    }

}

