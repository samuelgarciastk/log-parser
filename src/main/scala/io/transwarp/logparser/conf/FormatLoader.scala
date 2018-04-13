package io.transwarp.logparser.conf

import java.io.InputStream

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
  val logFormats: mutable.LinkedHashMap[String, LogFormat] = loadFormat(this.getClass.getClassLoader.getResourceAsStream("format.json"))

  /**
    * Load the configuration file for log format.
    *
    * @param inputStream InputStream of configuration file
    * @return LogFormat
    */
  def loadFormat(inputStream: InputStream): mutable.LinkedHashMap[String, LogFormat] = {
    val formatMap = new mutable.LinkedHashMap[String, LogFormat]
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val configMap = mapper.readValue[mutable.LinkedHashMap[String, mutable.LinkedHashMap[String, String]]](inputStream)
    configMap.foreach(f => formatMap += (f._1 -> LogFormat(f._2)))
    formatMap
  }
}
