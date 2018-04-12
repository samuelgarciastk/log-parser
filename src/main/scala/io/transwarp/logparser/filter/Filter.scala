package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/3
  */
abstract class Filter {
  val params: List[String]

  /**
    * Filter log entry.
    *
    * @param logEntry log entry
    * @return if the log entry needs to be saved, then return true, otherwise false.
    */
  def filter(logEntry: LogEntry): Boolean

  def copy(): Filter
}
