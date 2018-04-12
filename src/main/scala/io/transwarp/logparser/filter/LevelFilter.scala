package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/4
  *
  * @param params levels
  */
class LevelFilter(override val params: List[String]) extends Filter {
  override def filter(logEntry: LogEntry): Boolean = Some(logEntry).get.format("level").asInstanceOf[Option[String]] match {
    case Some(level) => params.contains(level)
    case None => false
  }

  override def copy(): Filter = new LevelFilter(params)
}
