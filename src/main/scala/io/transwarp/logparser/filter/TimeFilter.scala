package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
  * Author: stk
  * Date: 2018/4/4
  *
  * @param params [begin time, end time]
  */
class TimeFilter(override val params: List[String]) extends Filter {
  private val format = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss,SSS")

  override def filter(logEntry: LogEntry): Boolean = logEntry.format("timestamp").asInstanceOf[Option[DateTime]]
    .fold(false)(f => f.isAfter(DateTime.parse(params.head, format)) && f.isBefore(DateTime.parse(params.last, format)))

  override def copy(): Filter = new TimeFilter(params)
}
