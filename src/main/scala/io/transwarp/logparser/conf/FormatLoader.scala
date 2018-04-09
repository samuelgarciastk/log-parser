package io.transwarp.logparser.conf

import java.util

import org.yaml.snakeyaml.Yaml

import scala.collection.mutable
import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/8
  */
case class LogFormat(config: mutable.LinkedHashMap[String, String])

object FormatLoader {
  val logFormats: mutable.LinkedHashMap[String, LogFormat] = loadFormat()

  def loadFormat(): mutable.LinkedHashMap[String, LogFormat] = {
    val configString = Source.fromResource("format.yml").getLines.mkString("\n")
    val configMap = new Yaml().load(configString).asInstanceOf[util.LinkedHashMap[String, util.LinkedHashMap[String, String]]]
    val formatMap = new mutable.LinkedHashMap[String, LogFormat]
    configMap.forEach((k, v) => {
      val value = new mutable.LinkedHashMap[String, String]
      v.forEach((k, v) => value += (k -> v))
      formatMap += (k -> LogFormat(value))
    })
    formatMap
  }
}
