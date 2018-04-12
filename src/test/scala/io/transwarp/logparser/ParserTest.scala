package io.transwarp.logparser

import java.io.File
import java.nio.file.{Files, Path}

import io.transwarp.logparser.util.{LogWriter, ToolUtils}
import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/12
  */
class ParserTest {
  val tempDirectory: Path = Files.createTempDirectory("LP-")
  tempDirectory.toFile.deleteOnExit()
  val directory = "C:\\Users\\stk\\Downloads\\test-data\\logs\\inceptor"
  val logPath = "C:\\Users\\stk\\Downloads\\test-data\\final.log"
  val zipPath = "C:\\Users\\stk\\Downloads\\test-data\\logs.zip"

  @Test
  def logEntry(): Unit = {
    val files = ToolUtils.getAllFiles(new File(directory)).toList
    val (logEntries, duration: Double) = ToolUtils.calculateTime(Parser.merge(files))
    LogWriter.writeLogEntries(logEntries, logPath)
    println(duration)
  }

  @Test
  def logCase(): Unit = {
    val files = ToolUtils.getAllFiles(new File(directory)).toList
    val logEntries = Parser.merge(files)
    val logCases = Parser.getLogCases(logEntries)
    LogWriter.writeLogCases(logCases, logPath)
  }

  @Test
  def zipFile(): Unit = {
    ToolUtils.unzip(zipPath, tempDirectory)
    val files = ToolUtils.getAllFiles(tempDirectory.toFile).toList
    val logEntries = Parser.merge(files)
    LogWriter.writeLogEntries(logEntries, logPath)
  }
}
