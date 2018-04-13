package io.transwarp.logparser.filter

import java.text.SimpleDateFormat
import java.util.Date

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/4
  *
  * @param params [begin time, end time]
  */
class TimeFilter(override val params: List[String]) extends Filter {
  private val format = new SimpleDateFormat("yyyyMMdd HH:mm:ss,SSS")

  override def filter(logEntry: LogEntry): Boolean = logEntry.format("timestamp").asInstanceOf[Option[Date]]
    .fold(false)(f => f.compareTo(format.parse(params.head)) >= 0 && f.compareTo(format.parse(params.last)) <= 0)

  override def copy(): Filter = new TimeFilter(params)
}
