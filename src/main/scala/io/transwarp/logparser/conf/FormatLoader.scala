package io.transwarp.logparser.conf

import java.io.InputStream

import com.alibaba.fastjson.JSON

import scala.collection.mutable
import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/8
  */
case class LogFormat(config: mutable.LinkedHashMap[String, String])

object FormatLoader {
  val logFormats: mutable.LinkedHashMap[String, LogFormat] = loadFormat(this.getClass.getClassLoader.getResourceAsStream("format.yml"))

  /**
    * Load the configuration file for log format.
    *
    * @param inputStream InputStream of configuration file
    * @return LogFormat
    */
  def loadFormat(inputStream: InputStream): mutable.LinkedHashMap[String, LogFormat] = {
    val configFile = Source.fromInputStream(inputStream)
    val formatMap = new mutable.LinkedHashMap[String, LogFormat]
    val configMap = JSON.parseObject(configFile.getLines.mkString("\n"))
    println(configMap)
    configFile.close
    formatMap
  }
}
