package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntity

/**
  * Author: stk
  * Date: 2018/4/4
  */
class DuplicationFilter extends Filter {
  private var existed: Set[String] = Set()

  override def filter(logEntity: LogEntity): Boolean = Some(logEntity).get.duplicationIdentifier match {
    case Some(id) if !existed.contains(id) => existed += id; true
    case Some(id) if existed.contains(id) => false
    case None => true
  }
}
