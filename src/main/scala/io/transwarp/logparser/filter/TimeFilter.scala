package io.transwarp.logparser.filter

import java.text.SimpleDateFormat
import java.util.Date

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/4
  */
class TimeFilter(begin: String, end: String) extends Filter {
  private val format = new SimpleDateFormat("yyyyMMdd HH:mm:ss,SSS")

  override def filter(logEntry: LogEntry): Boolean = Some(logEntry).get.format("timestamp").asInstanceOf[Option[Date]] match {
    case Some(current) => current.compareTo(format.parse(begin)) >= 0 && current.compareTo(format.parse(end)) <= 0
    case None => false
  }
}
