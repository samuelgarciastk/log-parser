package io.transwarp.logparser

import java.io._
import java.nio.file.{Files, Path}
import java.util.Date

import io.transwarp.logparser.conf.Constant
import io.transwarp.logparser.filter.{Filter, LevelFilter, TimeFilter}
import io.transwarp.logparser.util.{LogCase, LogEntry, LogWriter, ToolUtils}

/**
  * Author: stk
  * Date: 2018/4/3
  */
object Parser {
  private val tempDirectory: Path = Files.createTempDirectory("LP-")
  tempDirectory.toFile.deleteOnExit()

  def main(args: Array[String]): Unit = {
    //    val zipPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\logs.zip"
    val directory = "C:\\Users\\stk\\Downloads\\test-data\\logs\\elasticsearch"
    val logPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\final.log"
    //    ToolUtils.unzip(zipPath, tempDirectory)
    val files = ToolUtils.getAllFiles(new File(directory)).toList
    val logEntries = mergeFilter(merge(files))
    val logCases = getLogCases(logEntries)
    LogWriter.writeLogCases(logCases, logPath)
  }

  def merge(files: List[File]): List[LogEntry] = {
    files.map(file => {
      val logEntries = FileParser.parseFile(file, setFileFilters())
      System.err.println(file.getName + ": " + logEntries.length + " records.")
      logEntries
    }).reduce((r1, r2) => (r1 ++ r2).sortBy(_.format("timestamp").asInstanceOf[Option[Date]]))
  }

  private def setFileFilters(): List[Filter] = List(
    new TimeFilter("20000101 00:00:00,000", "20500101 00:00:00,000"),
    new LevelFilter(List("WARN", "ERROR"))
  )

  def mergeFilter(logEntities: List[LogEntry]): List[LogEntry] = {
    val filters = setMergeFilters()
    logEntities.filter(l => filters.dropWhile(_.filter(l)).isEmpty)
  }

  private def setMergeFilters(): List[Filter] = List(
    //    new DuplicationFilter
  )

  def getLogCases(logEntities: List[LogEntry]): List[LogCase] = {
    if (logEntities.isEmpty) return null
    var logCases: List[LogCase] = List()
    var entities: List[LogEntry] = List()
    val iterator = logEntities.iterator
    val head = iterator.next
    entities = entities :+ head

    var lastTime: Long = head.format("timestamp").asInstanceOf[Option[Date]] match {
      case Some(time) => time.getTime
      case None => 0
    }

    do {
      val logEntity = iterator.next
      logEntity.format("timestamp").asInstanceOf[Option[Date]].foreach(time => {
        if (time.getTime - lastTime > Constant.CASE_INTERVAL) {
          logCases = logCases :+ new LogCase(entities)
          entities = List()
        }
        lastTime = time.getTime
      })
      entities = entities :+ logEntity
    } while (iterator.hasNext)
    logCases
  }
}
