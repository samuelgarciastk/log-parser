package io.transwarp.logparser.util

import java.io.File

import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/12
  */
class ToolUtilsTest {
  @Test
  def allFiles(): Unit = {
    val path = "C:\\Users\\stk\\Downloads\\test-data\\logs"
    ToolUtils.getAllFiles(new File(path)).foreach(println)
  }
}
