package io.transwarp.logparser

import java.io.File

import io.transwarp.logparser.filter.{LevelFilter, TimeFilter}
import io.transwarp.logparser.util.LogWriter
import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/11
  */
class FileParserTest {
  @Test
  def parseFile(): Unit = {
    val file = "C:\\Users\\stk\\Downloads\\test-data\\logs\\elasticsearch\\elasticsearch-2018-02-21.log"
    val path = "C:\\Users\\stk\\Downloads\\test-data\\logs\\test-result.log"
    LogWriter.writeLogEntries(FileParser.parseFile(new File(file), List(
      new TimeFilter("20000101 00:00:00,000", "20500101 00:00:00,000"),
      new LevelFilter(List("WARN", "ERROR"))
    )), path)
  }
}
