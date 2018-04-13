package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntry

/**
  * Author: stk
  * Date: 2018/4/4
  *
  * @param params levels
  */
class LevelFilter(override val params: List[String]) extends Filter {
  override def filter(logEntry: LogEntry): Boolean = logEntry.format("level").asInstanceOf[Option[String]]
    .fold(false)(params.contains(_))

  override def copy(): Filter = new LevelFilter(params)
}
