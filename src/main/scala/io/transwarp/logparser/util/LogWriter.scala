package io.transwarp.logparser.util

import java.io.{File, PrintWriter}

/**
  * Author: stk
  * Date: 2018/4/10
  */
object LogWriter {
  def writeLogEntries(logEntries: List[LogEntry], path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    logEntries.foreach(l => {
      val delimiter = getDelimiter(l.fileName.length)
      writer.write(delimiter + "\n" + l.fileName + ":\n")
      l.content.foreach(s => writer.write(s + "\n"))
    })
    writer.flush()
    writer.close()
    println("Write " + logEntries.length + " entries.")
  }

  def writeLogCases(logCases: List[LogCase], path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    var index = 0
    logCases.foreach(c => {
      if (c.exception.isDefined) {
        index += 1
        val lint = s"|| Log Case: $index ||"
        val delimiter = getDelimiter(lint.length)
        writer.write(s"$delimiter\n$lint\n$delimiter\n")
        writer.write(c.exception.get + "\n")
        /*c.content.foreach(l => {
          writer.write(l.fileName + ":\n")
          l.content.foreach(s => writer.write(s + "\n"))
        })*/
      }
    })
    writer.flush()
    writer.close()
    println("Write " + index + " cases.")
  }

  private def getDelimiter(length: Int): String = {
    var delimiter = ""
    for (_ <- 1 to length) yield delimiter += "="
    delimiter
  }

  def logEntriesToList(logEntries: List[LogEntry]): List[String] = {
    var result: List[String] = List()
    logEntries.foreach(l => {
      result = result :+ l.fileName + ": "
      l.content.foreach(s => result = result :+ s)
    })
    result
  }
}
