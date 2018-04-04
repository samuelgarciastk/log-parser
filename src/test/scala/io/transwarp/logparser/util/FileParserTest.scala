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
    val fileParser = new FileParser
    Parser.generate(fileParser.parseFile(new File("C:\\Users\\stk\\Downloads\\logs\\test\\test.log")), "C:\\Users\\stk\\Downloads\\logs\\test-result.log")
  }
}
