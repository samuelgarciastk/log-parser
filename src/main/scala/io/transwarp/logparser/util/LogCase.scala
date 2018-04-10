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

  val component: String = ""

  val exception: Option[String] = content.filter(_.isException).filter(_.duplicationIdentifier.isDefined)
    .map(e => e.fileName + ":\n" + e.content.head + "\n" + e.duplicationIdentifier.get + "\n").mkString("\n") match {
    case "" => None
    case s => Option(s)
  }
}
