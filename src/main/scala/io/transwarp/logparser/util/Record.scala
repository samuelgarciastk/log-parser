package io.transwarp.logparser.util

import java.text.SimpleDateFormat
import java.util.Date

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/3
  */
class Record(records: List[String]) {
  val content: List[String] = records match {
    case null | Nil => throw new IllegalArgumentException("Invalid record.")
    case _ => records
  }

  val isException: Boolean = content.exists(_.startsWith("\tat "))

  var timeRange: Int = 0

  val time: Option[Date] = Record.patterns.map(s => Converter.convertDateToRegex(s).r.findFirstIn(content.head) match {
    case Some(date) =>
      timeRange = s.length
      new SimpleDateFormat(s.replace("T", "'T'")).parse(date)
    case None => null
  }).find(_ != null).orElse(None)

  val level: Option[String] = ("(?<=[^\\w]+)(" + Record.levels.mkString("|") + ")(?=[^\\w]+)").r.findFirstIn(content.head)

  val duplicationIdentifier: Option[String] = content.find(_.startsWith("\tat ")) match {
    case Some(key) => val index = content.indexOf(key); Option(content(index - 1) + content(index))
    case None => Option(content.head.substring(timeRange))
  }
}

object Record {
  private val patterns: List[String] = Source.fromResource("pattern").getLines.toList
  private val levels: List[String] = Source.fromResource("level").getLines.toList
}
