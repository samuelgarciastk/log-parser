package io.transwarp.logparser.util

import java.io.{File, PrintWriter}

/**
  * Author: stk
  * Date: 2018/4/10
  */
object LogWriter {
  def writeLogEntries(logEntries: List[LogEntry], path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    logEntries.foreach(f => {
      val delimiter = getDelimiter(f.fileName.map(_.length).getOrElse(19) + 1)
      writer.write(delimiter + "\n" + f.fileName.getOrElse("File path not found") + ":\n")
      f.format.foreach { case (field, value) => writer.write("> " + field + ": " + value.getOrElse("NULL").toString + "\n") }
      writer.write("> Duplication Identifier: \n")
      writer.write(f.duplicationIdentifier.getOrElse("NULL") + "\n")
      writer.write("> Key Information: \n")
      writer.write(f.keyInfo.getOrElse("NULL") + "\n")
      writer.write("> Content: \n")
      f.content.foreach(s => writer.write(s + "\n"))
    })
    writer.flush()
    writer.close()
    println("Write " + logEntries.length + " entries.")
  }

  def writeLogCases(logCases: List[LogCase], path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    var index = 0
    logCases.foreach(f => {
      if (f.keyInfo.isDefined) {
        index += 1
        val lint = s"|| Log Case: $index ||"
        val delimiter = getDelimiter(lint.length)
        writer.write(s"$delimiter\n$lint\n$delimiter\n")
        writer.write(f.keyInfo.get + "\n")
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
}
