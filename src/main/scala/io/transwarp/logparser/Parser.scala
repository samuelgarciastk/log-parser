package io.transwarp.logparser

import java.io._

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import io.transwarp.logparser.conf.Constant
import io.transwarp.logparser.filter.{DuplicationFilter, Filter}
import io.transwarp.logparser.util.{LogCase, LogEntry, ToolUtils}
import org.joda.time.DateTime

import scala.collection.mutable

/**
  * Author: stk
  * Date: 2018/4/3
  */
object Parser {
  var fileFilters: List[Filter] = getFilters(ToolUtils.fileToString("conf.json"))
  var mergeFilters: List[Filter] = List(new DuplicationFilter(null))

  def merge(files: List[File]): List[LogEntry] = files.par.map(f => FileParser.parseFile(f, cloneFilters(fileFilters)))
    .reduce((l, r) => (l ++ r).sortBy(_.format("timestamp").asInstanceOf[Option[DateTime]].getOrElse(new DateTime(0)).getMillis))

  def cloneFilters(filters: List[Filter]): List[Filter] = {
    var newFilters: List[Filter] = List()
    filters.foreach(f => newFilters = newFilters :+ f.copy)
    newFilters
  }

  def getLogCases(logEntries: List[LogEntry]): List[LogCase] = {
    if (logEntries.isEmpty) return null
    var logCases: List[LogCase] = List()
    var entries: List[LogEntry] = List()
    val iterator = logEntries.iterator
    val head = iterator.next
    entries = entries :+ head
    var lastTime: Long = head.format("timestamp").asInstanceOf[Option[DateTime]].fold(0L)(_.getMillis)

    while (iterator.hasNext) {
      val logEntry = iterator.next
      logEntry.format("timestamp").asInstanceOf[Option[DateTime]].foreach(f => {
        if (f.getMillis - lastTime > Constant.CASE_INTERVAL) {
          logCases = logCases :+ new LogCase(entries)
          entries = List()
        }
        lastTime = f.getMillis
      })
      entries = entries :+ logEntry
    }
    logCases = logCases :+ new LogCase(entries)
    logCases
  }

  def mergeFilter(logEntities: List[LogEntry]): List[LogEntry] = logEntities.filter(p => mergeFilters.dropWhile(_.filter(p)).isEmpty)

  private def getFilters(json: String): List[Filter] = {
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val config = mapper.readValue[mutable.LinkedHashMap[String, List[String]]](json)
    var filters: List[Filter] = List()
    config.foreach { case (filterName, parameters) => filters = filters :+
      Class.forName("io.transwarp.logparser.filter." + filterName)
        .getConstructor(classOf[List[String]]).newInstance(parameters).asInstanceOf[Filter]
    }
    filters
  }
}
