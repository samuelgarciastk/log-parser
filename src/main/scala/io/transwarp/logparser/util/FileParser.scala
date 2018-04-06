package io.transwarp.logparser.util

import java.io.File

import io.transwarp.logparser.filter._

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/4
  */
object FileParser {
  private val pattern: List[String] = Source.fromResource("pattern").getLines.map(Converter.convertDateToRegex).toList

  def parseFile(file: File): List[Record] = {
    val filters = List(new TimeFilter("20000101 00:00:00,000", "20500101 00:00:00,000"),
      new LevelFilter)

    println("Parsing file: " + file)
    var records: List[Record] = List()
    var fileLines = Source.fromFile(file).getLines.toList
    fileLines = fileLines.dropWhile(!isBeginLine(_))
    var lines: List[String] = List()

    val filter = (lines: List[String]) => if (lines.nonEmpty) {
      val record = new Record(lines)
      if (filters.dropWhile(_.filter(record)).isEmpty) records = records :+ record
    }

    for (line <- fileLines) {
      if (isBeginLine(line)) {
        filter(lines)
        lines = List()
      }
      lines = lines :+ line
    }
    filter(lines)
    records
  }

  private def isBeginLine(line: String): Boolean = pattern.map(_.r.findFirstIn(line)).exists(_.isDefined)
}
