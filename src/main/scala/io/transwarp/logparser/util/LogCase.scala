package io.transwarp.logparser.util

/**
  * Author: stk
  * Date: 2018/4/10
  *
  * One log case is a list of log entries whose timestamps are in a specific time range.
  */
class LogCase(logEntries: List[LogEntry]) {
  val content: List[LogEntry] = if (Option(logEntries).getOrElse(Nil).isEmpty) throw new IllegalArgumentException("Invalid entries.") else logEntries

  val component: String = ""

  val exception: Option[String] = content.filter(_.isException).filter(_.duplicationIdentifier.isDefined)
    .map(f => f.fileName.getOrElse("File path not found") + ":\n" + f.content.head + "\n" + f.duplicationIdentifier.get + "\n").mkString("\n") match {
    case "" => None
    case s => Option(s)
  }
}
