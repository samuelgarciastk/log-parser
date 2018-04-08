package io.transwarp.logparser.conf

import org.yaml.snakeyaml.Yaml

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/8
  */
object ConfLoader {
  def loadFormat(): Unit = {
    val confFile = this.getClass.getClassLoader.getResourceAsStream("format")
    val yaml = new Yaml
    val a = yaml.load(confFile)
    println(a)
  }
}
