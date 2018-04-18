package io.transwarp.logparser.conf

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.collection.mutable

/**
  * Author: stk
  * Date: 2018/4/8
  */
case class LogFormat(config: mutable.LinkedHashMap[String, String])

object FormatLoader {
  val logFormats: Map[String, LogFormat] = loadFormat()

  /**
    * Load the default configuration file for log format.
    *
    * @return LogFormat
    */
  def loadFormat(): Map[String, LogFormat] = {
    val inputStream = this.getClass.getClassLoader.getResourceAsStream("format.json")
    var formatMap: Map[String, LogFormat] = Map()
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val configMap = mapper.readValue[Map[String, mutable.LinkedHashMap[String, String]]](inputStream)
    configMap.foreach { case (name, format) => formatMap += (name -> LogFormat(format)) }
    formatMap
  }
}
