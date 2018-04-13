package io.transwarp.logparser.util

import io.transwarp.logparser.conf.LogFormat
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.util.Try

/**
  * Author: stk
  * Date: 18/4/8
  */
class LogEntry(records: List[String], logFormat: LogFormat) {
  val content: List[String] = if (Option(records).getOrElse(Nil).isEmpty) throw new IllegalArgumentException("Invalid records.") else records

  val format: Map[String, Option[Any]] = {
    var head = content.head
    var map: Map[String, Option[Any]] = Map()
    logFormat.config.foreach(c => c._1 match {
      case "timestamp" => map += (c._1 -> Converter.convertDateToRegex(c._2).r.findFirstIn(head).map(f => {
        head = head.replaceAllLiterally(f, "")
        DateTime.parse(f, DateTimeFormat.forPattern(Converter.dateEscape(c._2)))
      }))
      case _ => map += (c._1 -> c._2.r.findFirstIn(head).map(f => {
        head = head.replaceAllLiterally(f, "")
        Try(c._2.r.replaceFirstIn(f, "$1").trim).getOrElse(c._2.r.replaceFirstIn(f, "$0").trim)
      }))
    })
    map += ("message" -> Option(head.replaceFirst("[^\\w]*", "").trim))
    map
  }

  val isException: Boolean = content.exists(_.startsWith("\tat "))

  /*val duplicationIdentifier: Option[String] = content.find(_.startsWith("\tat ")) match {
    case Some(id) => val index = content.indexOf(id); Option(content(index - 1) + "\n" + content(index))
    case None => format("message").asInstanceOf[Option[String]]
  }*/

  val duplicationIdentifier: Option[String] = None

  var fileName: Option[String] = None
}
