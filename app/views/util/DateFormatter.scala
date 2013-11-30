package views.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


object DateFormatter {

  def jdt(date: DateTime, pattern: String = "dd/MM/yyyy hh:mm") = DateTimeFormat.forPattern(pattern).print(date)

  def jd(date: DateTime, pattern: String = "dd/MM/yyyy") = DateTimeFormat.forPattern(pattern).print(date)

}
