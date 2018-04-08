package io.transwarp.logparser.conf

import java.util

import io.transwarp.logparser.conf.LogFormatProtocol._
import net.jcazevedo.moultingyaml._
import org.yaml.snakeyaml.Yaml

import scala.collection.mutable
import scala.io.Source
import scala.collection.JavaConverters._
import java.util.LinkedHashMap

/**
  * Author: stk
  * Date: 2018/4/8
  */
object ConfigLoader {
//  def loadFormat(): List[LogFormat] = {
//    val configString = Source.fromResource("format.yml").getLines.mkString("\n")
//    configString.parseYamls.map(_.convertTo[LogFormat]).toList
//  }

  def loadFormat(): util.LinkedHashMap[String, util.LinkedHashMap[String, String]] = {
    val configString = Source.fromResource("format.yml").getLines.mkString("\n")
    new Yaml().load(configString).asInstanceOf[util.LinkedHashMap[String, util.LinkedHashMap[String, String]]]
  }
}
