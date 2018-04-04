package io.transwarp.logparser.filter

import java.text.SimpleDateFormat

import io.transwarp.logparser.util.Record

/**
  * Author: stk
  * Date: 2018/4/4
  */
class TimeFilter(begin: String, end: String) extends Filter {
  private val format = new SimpleDateFormat("yyyyMMdd HH:mm:ss,SSS")

  override def filter(record: Record): Boolean = Some(record).get.time match {
    case Some(current) => current.compareTo(format.parse(begin)) >= 0 && current.compareTo(format.parse(end)) <= 0
    case None => false
  }
}
