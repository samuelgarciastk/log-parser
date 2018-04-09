package io.transwarp.logparser.filter

import io.transwarp.logparser.util.LogEntity

/**
  * Author: stk
  * Date: 2018/4/3
  */
class ExceptionFilter extends Filter {
  override def filter(logEntity: LogEntity): Boolean = Some(logEntity).exists(_.isException)
}
