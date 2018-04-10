package io.transwarp.logparser.util

/**
  * Author: stk
  * Date: 2018/4/10
  */
class LogCase(logEntries: List[LogEntry]) {
  val content: List[LogEntry] = logEntries match {
    case null | Nil => throw new IllegalArgumentException("Invalid entries.")
    case _ => logEntries
  }

  //  val attributes
}
