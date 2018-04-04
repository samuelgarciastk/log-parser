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
    val folderPath = "C:\\Users\\stk\\Downloads\\logs\\transwarp-manager"
    val logPath = "C:\\Users\\stk\\Downloads\\logs\\final.log"
    generate(merge(parseFolder(folderPath)), logPath)
  }

  def parseFolder(path: String): List[File] = new File(path).listFiles.filter(_.isFile).filter(_.getName.endsWith(".log")).filter(isExceptionLog).toList

  private def isExceptionLog(file: File): Boolean = Source.fromFile(file).getLines().exists(_.startsWith("\tat "))

  def merge(files: List[File]): List[Record] = {
    val fileParser = new FileParser
    files.par.map(file => {
      val records = fileParser.parseFile(file)
      println(file.getName + ": " + records.length + " records.")
      records
    }).reduce(mergeRecords)
  }

  private def mergeRecords(record1: List[Record], record2: List[Record]): List[Record] = (record1 ++ record2).sortBy(_.time)

  def generate(records: List[Record], path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    records.foreach(_.content.foreach(s => writer.write(s + "\n")))
    writer.flush()
    println("Write " + records.length + " records.")
    writer.close()
  }
}
