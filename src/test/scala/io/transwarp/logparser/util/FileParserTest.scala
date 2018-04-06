package io.transwarp.logparser.util

import java.io.File

import io.transwarp.logparser.Parser
import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/4
  */
class FileParserTest {
  @Test
  def parseFile(): Unit = {
    Parser.generate(FileParser.parseFile(new File("C:\\Users\\stk\\Downloads\\test-data\\logs\\elasticsearch\\elasticsearch-2018-03-19.log")),
      "C:\\Users\\stk\\Downloads\\test-data\\logs\\test-result.log")
  }
}
