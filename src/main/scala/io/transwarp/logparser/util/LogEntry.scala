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
    logFormat.config.foreach { case (field, regex) =>
      field match {
        case "timestamp" => map += (field -> Converter.convertDateToRegex(regex).r.findFirstIn(head).map(f => {
          head = head.replaceAllLiterally(f, "")
          DateTime.parse(f, DateTimeFormat.forPattern(Converter.dateEscape(regex)))
        }))
        case _ => map += (field -> regex.r.findFirstIn(head).map(f => {
          head = head.replaceAllLiterally(f, "")
          Try(regex.r.replaceFirstIn(f, "$1").trim).getOrElse(regex.r.replaceFirstIn(f, "$0").trim)
        }))
      }
    }
    map += ("message" -> Option(head.replaceFirst("[^\\w]*", "").trim))
    map
  }

  val isException: Boolean = content.exists(_.startsWith("\tat "))

  val (duplicationIdentifier: Option[String], keyInfo: Option[String]) = content.find(_.startsWith("\tat "))
    .fold({
      val msg = format("message").asInstanceOf[Option[String]]
      (msg, msg)
    })(f => {
      val msg = format("message").asInstanceOf[Option[String]]
      val index = content.indexOf(f)
      val exception = Option(content(index - 1) + "\n" + content(index))
      if (exception.isEmpty && msg.isEmpty) (exception, None)
      else {
        var key = ""
        if (msg.isEmpty) key = exception.getOrElse("")
        else key = msg.getOrElse("") + "\n" + exception.getOrElse("")
        (exception, Option(key))
      }
    })

  var fileName: Option[String] = None
}
