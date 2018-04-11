package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/3
  */
trait Filter {
  /**
    * Filter log entry.
    *
    * @param logEntry log entry
    * @return if the log entry needs to be saved, then return true.
    */
  def filter(logEntry: LogEntry): Boolean
}
