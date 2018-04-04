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

  val isException: Boolean = content.par.exists(p => p.startsWith("\tat "))

  val time: Option[Date] = Source.fromResource("pattern").getLines().toList
    .map(s => Converter.convertDateToRegex(s).r.findFirstIn(content.head) match {
      case Some(date) => new SimpleDateFormat(s.replace("T", "'T'")).parse(date)
      case None => null
    }).find(_ != null).orElse(None)

  val level: Option[String] = ("(?<=[^\\w]+)(" + Source.fromResource("level").getLines.mkString("|") + ")(?=[^\\w]+)").r.findFirstIn(content.head)

  val duplicationIdentifier: Option[String] = content.find(s => s.startsWith("\tat ")) match {
    case Some(key) => val index = content.indexOf(key); Option(content(index - 1) + content(index))
    case None => None
  }
}
