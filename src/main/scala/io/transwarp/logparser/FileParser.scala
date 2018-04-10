package io.transwarp.logparser

import java.io.File

import io.transwarp.logparser.conf.{FormatLoader, LogFormat}
import io.transwarp.logparser.filter.Filter
import io.transwarp.logparser.util.{Converter, LogEntry}

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/10
  */
object FileParser {
  def parseFile(file: File, filters: List[Filter]): List[LogEntry] = {
    println("Parsing file: " + file)

    var logEntries: List[LogEntry] = List()
    val fileLines = Source.fromFile(file).getLines.toList
    val logFormat = identifyFormat(fileLines.head)
    var lines: List[String] = List()

    val filter = (lines: List[String]) => if (lines.nonEmpty) {
      val logEntity = new LogEntry(lines, logFormat)
      if (filters.dropWhile(_.filter(logEntity)).isEmpty) logEntries = logEntries :+ logEntity
    }

    val isBeginLine = (line: String) => logFormat.config.head match {
      case head if head._1 == "timestamp" => ("^" + Converter.convertDateToRegex(head._2)).r.findFirstIn(line).isDefined
      case head => ("^" + head._2).r.findFirstIn(line).isDefined
    }

    for (line <- fileLines) {
      if (isBeginLine(line)) {
        filter(lines)
        lines = List()
      }
      lines = lines :+ line
    }
    filter(lines)
    logEntries.foreach(_.fileName = file.toString)
    logEntries
  }

  def identifyFormat(head: String): LogFormat = FormatLoader.logFormats
    .map(f => (f._2, new LogEntry(List(head), f._2).format.count(_._2.isDefined))).toList
    .sortBy(_._2)(Ordering[Int].reverse).head._1
}
