package io.transwarp.logparser.conf

import net.jcazevedo.moultingyaml.DefaultYamlProtocol

/**
  * Author: stk
  * Date: 18/4/8
  */
case class LogFormat(name: String, timestamp: String, level: String, logger: String)

object LogFormatProtocol extends DefaultYamlProtocol {
  implicit val format = yamlFormat4(LogFormat)
}
