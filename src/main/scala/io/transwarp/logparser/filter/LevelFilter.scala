package io.transwarp.logparser.filter

import io.transwarp.logparser.util.Record

/**
  * Author: stk
  * Date: 2018/4/4
  */
class LevelFilter extends Filter {
  override def filter(record: Record): Boolean = Some(record).get.level match {
    case Some(level) => level == "WARN" || level == "ERROR"
    case None => false
  }
}
