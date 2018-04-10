package io.transwarp.logparser.util

import java.text.SimpleDateFormat
import java.util.Date

import io.transwarp.logparser.conf.LogFormat

/**
  * Author: stk
  * Date: 18/4/8
  */
class LogEntry(records: List[String], logFormat: LogFormat) {
  val content: List[String] = records match {
    case null | Nil => throw new IllegalArgumentException("Invalid records.")
    case _ => records
  }

  val format: Map[String, Option[Any]] = {
    var head = content.head
    var map: Map[String, Option[Any]] = Map()
    logFormat.config.foreach(c => c._1 match {
      case "timestamp" =>
        val value: Option[Date] = Converter.convertDateToRegex(c._2).r.findFirstIn(head) match {
          case Some(time) =>
            head = head.replaceAllLiterally(time, "")
            Option(new SimpleDateFormat(Converter.dateEscape(c._2)).parse(time))
          case None => None
        }
        map += (c._1 -> value)
      case "level" =>
        val value: Option[String] = c._2.r.findFirstIn(head) match {
          case Some(level) =>
            head = head.replaceAllLiterally(level, "")
            Option(try {
              c._2.r.replaceFirstIn(level, "$1").trim
            } catch {
              case _: Throwable => c._2.r.replaceFirstIn(level, "$0").trim
            })
          case None => None
        }
        map += (c._1 -> value)
      case "logger" =>
        val value: Option[String] = c._2.r.findFirstIn(head) match {
          case Some(logger) =>
            head = head.replaceAllLiterally(logger, "")
            Option(try {
              c._2.r.replaceFirstIn(logger, "$1").trim
            } catch {
              case _: Throwable => c._2.r.replaceFirstIn(logger, "$0").trim
            })
          case None => None
        }
        map += (c._1 -> value)
    })
    map += ("message" -> Option(head.trim))
    map
  }

  val isException: Boolean = content.exists(_.startsWith("\tat "))

  val duplicationIdentifier: Option[String] = content.find(_.startsWith("\tat ")) match {
    case Some(id) => val index = content.indexOf(id); Option(content(index - 1) + "\n" + content(index))
    case None => format("message").asInstanceOf[Option[String]]
  }

  var fileName: String = _
}
