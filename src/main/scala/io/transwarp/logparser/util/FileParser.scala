package io.transwarp.logparser.util

import java.io.File

import io.transwarp.logparser.filter.{Filter, LevelFilter, TimeFilter}

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/4
  */
class FileParser {
  private val pattern: List[String] = Source.fromResource("pattern").getLines().map(Converter.convertDateToRegex).toList
  private var filters: List[Filter] = List()

  def parseFile(file: File): List[Record] = {
    initFilters()
    println("Parsing file: " + file)
    var records: List[Record] = List()
    var fileLines = Source.fromFile(file).getLines.toList
    fileLines = fileLines.dropWhile(!isBeginLine(_))
    var lines: List[String] = List()
    for (line <- fileLines) {
      if (isBeginLine(line)) {
        if (lines.nonEmpty) {
          val record = new Record(lines)
          if (filter(record)) records = records :+ record
        }
        lines = List()
      }
      lines = lines :+ line
    }
    records
  }

  private def initFilters(): Unit = {
    filters = List(new TimeFilter("20000101 00:00:00,000", "20500101 00:00:00,000"),
      new LevelFilter)
  }

  private def isBeginLine(line: String): Boolean = pattern.map(_.r.findFirstIn(line)).exists(_.isDefined)

  private def filter(record: Record): Boolean = filters.dropWhile(_.filter(record)).isEmpty
}
