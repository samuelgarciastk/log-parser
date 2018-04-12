package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/4
  */
class DuplicationFilter(override val params: List[String]) extends Filter {
  private var existed: Set[String] = Set()

  override def filter(logEntry: LogEntry): Boolean = Some(logEntry).get.duplicationIdentifier match {
    case Some(id) if !existed.contains(id) => existed += id; true
    case Some(id) if existed.contains(id) => false
    case None => true
  }

  override def copy(): Filter = new DuplicationFilter(params)
}
