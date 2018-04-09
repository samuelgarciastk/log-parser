package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntity

/**
  * Author: stk
  * Date: 2018/4/4
  */
class LevelFilter(levels: List[String]) extends Filter {
  override def filter(logEntity: LogEntity): Boolean = Some(logEntity).get.config("level").asInstanceOf[Option[String]] match {
    case Some(level) => levels.contains(level)
    case None => false
  }
}
