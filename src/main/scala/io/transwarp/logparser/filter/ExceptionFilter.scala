package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/3
  */
class ExceptionFilter extends Filter {
  override def filter(logEntry: LogEntry): Boolean = Some(logEntry).exists(_.isException)
}
