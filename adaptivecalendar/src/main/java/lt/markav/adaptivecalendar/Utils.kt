package lt.markav.adaptivecalendar

import org.joda.time.DateTime

fun DateTime.firstOfMonth(): DateTime = DateTime(this.year, this.monthOfYear, 1, 0, 0, 0)
fun DateTime.printMonth(): String = this.toString("yyyy-MM")
