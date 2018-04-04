package io.transwarp.logparser.filter

import io.transwarp.logparser.util.Record

/**
  * Author: stk
  * Date: 2018/4/4
  */
class DuplicationFilter extends Filter {
  private var existed: Set[String] = Set()

  override def filter(record: Record): Boolean = Some(record).get.duplicationIdentifier match {
    case Some(id) if !existed.contains(id) => existed += id; true
    case Some(id) if existed.contains(id) => false
    case None => false
  }
}
