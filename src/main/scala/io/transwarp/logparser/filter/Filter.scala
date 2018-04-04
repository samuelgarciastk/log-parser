package io.transwarp.logparser.filter

import io.transwarp.logparser.util.Record

/**
  * Author: stk
  * Date: 2018/4/3
  */
trait Filter {
  def filter(record: Record): Boolean
}
