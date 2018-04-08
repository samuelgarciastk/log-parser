package io.transwarp.logparser.util

import java.text.SimpleDateFormat
import java.util.Date

import io.transwarp.logparser.conf.LogFormat

/**
  * Author: stk
  * Date: 18/4/8
  */
class LogEntry(records: List[String], logFormat: LogFormat) {
  val content: List[String] = records match {
    case null | Nil => throw new IllegalArgumentException("Invalid record.")
    case _ => records
  }

  private var head = content.head

  val timestamp: Option[Date] = Converter.convertDateToRegex(logFormat.timestamp).r.findFirstIn(head) match {
    case Some(date) =>
      head = head.replaceFirst(date, "")
      Option(new SimpleDateFormat(Converter.dateEscape(logFormat.timestamp)).parse(date))
    case None => None
  }



  val isException: Boolean = content.exists(_.startsWith("\tat "))
}
