package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/3
  */
trait Filter {
  def filter(logEntry: LogEntry): Boolean
}
