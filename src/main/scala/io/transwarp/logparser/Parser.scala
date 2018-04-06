package io.transwarp.logparser

import java.io.{File, PrintWriter}

import io.transwarp.logparser.util.{FileParser, Record}

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/3
  */
object Parser {
  def main(args: Array[String]): Unit = {
    val folderPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\elasticsearch"
    val logPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\final.log"
    generate(merge(parseFolder(folderPath)), logPath)
  }

  def parseFolder(path: String): List[File] = new File(path).listFiles
    .filter(_.isFile)
    .filter(_.getName.endsWith(".log"))
    .filter(Source.fromFile(_).getLines.exists(_.startsWith("\tat "))).toList

  def merge(files: List[File]): List[Record] = {
    files.par.map(file => {
      val records = FileParser.parseFile(file)
      println(file.getName + ": " + records.length + " records.")
      records
    }).reduce((r1, r2) => (r1 ++ r2).sortBy(_.time))
  }

  def generate(records: List[Record], path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    records.foreach(_.content.foreach(s => writer.write(s + "\n")))
    writer.flush()
    println("Write " + records.length + " records.")
    writer.close()
  }
}
