package io.transwarp.logparser.filter

import io.transwarp.logparser.util.Record

/**
  * Author: stk
  * Date: 2018/4/3
  */
class ExceptionFilter extends Filter {
  override def filter(record: Record): Boolean = Some(record).exists(_.isException)
}
