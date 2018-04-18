package io.transwarp.logparser

import java.io.File

import io.transwarp.logparser.conf.{FormatLoader, LogFormat}
import io.transwarp.logparser.filter.Filter
import io.transwarp.logparser.util.{Converter, LogEntry}

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/10
  *
  * Parse a single log file.
  */
object FileParser {
  def parseFile(file: File, filters: List[Filter]): List[LogEntry] = {
    println("Parsing file: " + file)
    var logEntries: List[LogEntry] = List()
    val reader = Source.fromFile(file)
    val fileLines = reader.getLines
    var line = ""
    if (fileLines.hasNext) line = fileLines.next else return List()
    val logFormat = identifyFormat(line)
    var lines: List[String] = List(line)

    val filter = (lines: List[String]) => if (lines.nonEmpty) {
      val logEntry = new LogEntry(lines, logFormat)
      logEntry.fileName = Some(file.toString)
      if (filters.dropWhile(_.filter(logEntry)).isEmpty) logEntries = logEntries :+ logEntry
    }

    val isBeginLine = (line: String) => logFormat.config.head match {
      case head if head._1 == "timestamp" => ("^" + Converter.convertDateToRegex(head._2)).r.findFirstIn(line).isDefined
      case head => ("^" + head._2).r.findFirstIn(line).isDefined
    }

    while (fileLines.hasNext) {
      line = fileLines.next
      if (isBeginLine(line)) {
        filter(lines)
        lines = List()
      }
      lines = lines :+ line
    }
    filter(lines)
    reader.close
    println(file + ": " + logEntries.length + " entries.")
    logEntries
  }

  def identifyFormat(head: String): LogFormat = FormatLoader.logFormats
    .map { case (_, format) => (format, {
      val entry = new LogEntry(List(head), format)
      entry.format.size - entry.format.count(_._2.isEmpty)
    })
    }.toList.maxBy(_._2)._1
}
