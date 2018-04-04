package io.transwarp.logparser.util

import java.util.Date

/**
  * Author: stk
  * Date: 2018/4/3
  */
class Record (records: List[String]) {
  var content: List[String] = _
  var isException: Option[Boolean] = _
  var time: Option[Date] = _
  var level: Option[String] = _
  var duplicationIdentifier: Option[String] = _

  if (records == null || records.isEmpty) throw new IllegalArgumentException("Invalid record.")
  content = records

  def setIsException(): Unit = {
    isException = Option.apply(content.par.exists(p => p.startsWith("\tat ")))
  }
}
