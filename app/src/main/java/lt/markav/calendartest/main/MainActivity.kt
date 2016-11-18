package lt.markav.calendartest.main

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import lt.markav.adaptivecalendar.CalendarAdapter
import lt.markav.calendartest.R
import org.jetbrains.anko.onClick
import org.jetbrains.anko.textColor
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendar.setAdapter(CA())
        calendar.onMonthSelected { }
        calendar.onDayClicked { view, dateTime ->  }

        back.onClick { calendar.setCurrentMonth(calendar.getCurrentMonth().minusMonths(1)) }
        next.onClick { calendar.setCurrentMonth(calendar.getCurrentMonth().plusMonths(1)) }

        toast.onClick { toast(calendar.getCurrentMonth().toString("yyyy-MM")) }
    }

}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

class CA() : CalendarAdapter() {

    init {
        monthFormat = DateTimeFormat.forPattern("yyyy - MMMM")
        locale = Locale("lt")
    }

    override fun loadDataForMonth(month: DateTime) {
        if (month.monthOfYear == 10) {
            dataLoadedForMonth(month, "")
        } else {
            super.loadDataForMonth(month)
        }
    }

    override fun updateDayView(view: View, date: DateTime, thisMonth: Boolean, data: Any?) {
        if (data != null) {
            (view as TextView).textColor = 0xFF0066FF.toInt()
        }
    }

}

