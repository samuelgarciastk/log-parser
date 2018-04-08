package io.transwarp.logparser.conf

import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/8
  */
class ConfigLoaderTest {
  @Test
  def loadFormat(): Unit = {
    val map = ConfigLoader.loadFormat()
    println(map)
  }
}
