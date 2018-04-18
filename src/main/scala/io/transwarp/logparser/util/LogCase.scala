package io.transwarp.logparser.util

/**
  * Author: stk
  * Date: 2018/4/10
  *
  * One log case is a list of log entries whose timestamps are in a specific time range.
  */
class LogCase(logEntries: List[LogEntry]) {
  val content: List[LogEntry] = if (Option(logEntries).getOrElse(Nil).isEmpty) throw new IllegalArgumentException("Invalid entries.") else logEntries

  val keyInfo: Option[String] = content.filter(_.keyInfo.isDefined).map(_.keyInfo.get).reduce(_ + "\n" + _) match {
    case "" => None
    case s => Option(s)
  }
}
