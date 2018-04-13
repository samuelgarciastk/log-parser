package io.transwarp.logparser

import java.io._
import java.util.Date

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import io.transwarp.logparser.conf.Constant
import io.transwarp.logparser.filter.Filter
import io.transwarp.logparser.util.{LogCase, LogEntry, ToolUtils}

import scala.collection.mutable

/**
  * Author: stk
  * Date: 2018/4/3
  */
object Parser {
  var fileFilters: List[Filter] = getFilters(ToolUtils.fileToString("conf.json"))
  var mergeFilters: List[Filter] = Nil

  def merge(files: List[File]): List[LogEntry] = files.par.map(f => FileParser.parseFile(f, cloneFilters(fileFilters)))
    .reduce((l, r) => (l ++ r).sortBy(_.format("timestamp").asInstanceOf[Option[Date]]))

  def cloneFilters(filters: List[Filter]): List[Filter] = {
    var newFilters: List[Filter] = List()
    filters.foreach(f => newFilters = newFilters :+ f.copy)
    newFilters
  }

  def getLogCases(logEntities: List[LogEntry]): List[LogCase] = {
    if (logEntities.isEmpty) return null
    var logCases: List[LogCase] = List()
    var entities: List[LogEntry] = List()
    val iterator = logEntities.iterator
    val head = iterator.next
    entities = entities :+ head
    var lastTime: Long = head.format("timestamp").asInstanceOf[Option[Date]].fold(0L)(_.getTime)

    do {
      val logEntity = iterator.next
      logEntity.format("timestamp").asInstanceOf[Option[Date]].foreach(f => {
        if (f.getTime - lastTime > Constant.CASE_INTERVAL) {
          logCases = logCases :+ new LogCase(entities)
          entities = List()
        }
        lastTime = f.getTime
      })
      entities = entities :+ logEntity
    } while (iterator.hasNext)
    logCases
  }

  def mergeFilter(logEntities: List[LogEntry]): List[LogEntry] = logEntities.filter(p => mergeFilters.dropWhile(_.filter(p)).isEmpty)

  private def getFilters(json: String): List[Filter] = {
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val config = mapper.readValue[mutable.LinkedHashMap[String, List[String]]](json)
    var filters: List[Filter] = List()
    config.foreach(f => filters = filters :+
      Class.forName("io.transwarp.logparser.filter." + f._1)
        .getConstructor(classOf[List[String]]).newInstance(f._2).asInstanceOf[Filter])
    filters
  }
}
