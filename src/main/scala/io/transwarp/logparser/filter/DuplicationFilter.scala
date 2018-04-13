package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/4
  */
class DuplicationFilter(override val params: List[String]) extends Filter {
  private var existed: Set[String] = Set()

  override def filter(logEntry: LogEntry): Boolean = logEntry.duplicationIdentifier
    .fold(true)(f => if (existed.contains(f)) false else {
      existed += f
      true
    })

  override def copy(): Filter = new DuplicationFilter(params)
}
