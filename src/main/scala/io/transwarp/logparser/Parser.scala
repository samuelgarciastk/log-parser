package io.transwarp.logparser

import java.io._
import java.nio.file.{Files, Path}
import java.util.Date
import java.util.zip.ZipInputStream

import io.transwarp.logparser.conf.{FormatLoader, LogFormat}
import io.transwarp.logparser.filter.{DuplicationFilter, Filter, LevelFilter, TimeFilter}
import io.transwarp.logparser.util.{Converter, LogEntity}

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/3
  */
object Parser {
  private val tempDirectory: Path = Files.createTempDirectory("LP-")
  tempDirectory.toFile.deleteOnExit()

  def main(args: Array[String]): Unit = {
    val zipPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\logs.zip"
    val directory = "C:\\Users\\stk\\Downloads\\test-data\\logs\\elasticsearch"
    val logPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\final.log"
    //    unzip(zipPath, tempDirectory)
    //    val logEntities = mergeFilter(merge(getAllFiles(tempDirectory.toFile).toList))
    val logEntities = mergeFilter(merge(getAllFiles(new File(directory)).toList))
    generate(logEntities, logPath)
  }

  def unzip(file: String, path: Path): Unit = {
    val in = new ZipInputStream(new FileInputStream(file))
    Stream.continually(in.getNextEntry).takeWhile(_ != null).foreach(file => {
      if (!file.isDirectory) {
        val outPath = path.resolve(file.getName)
        val parent = outPath.getParent
        if (!parent.toFile.exists) parent.toFile.mkdirs
        val out = new FileOutputStream(outPath.toFile)
        val buffer = new Array[Byte](4096)
        Stream.continually(in.read(buffer)).takeWhile(_ != -1).foreach(out.write(buffer, 0, _))
      }
    })
  }

  def getAllFiles(file: File): Array[File] = {
    val files = file.listFiles
    files.filter(_.isFile).filter(_.getName.endsWith(".log"))
      .filter(Source.fromFile(_).getLines.exists(_.startsWith("\tat "))) ++
      files.filter(_.isDirectory).flatMap(getAllFiles)
  }

  def merge(files: List[File]): List[LogEntity] = {
    files.map(file => {
      val logEntry = parseFile(file, setFileFilters())
      System.err.println(file.getName + ": " + logEntry.length + " records.")
      logEntry
    }).reduce((r1, r2) => (r1 ++ r2).sortBy(_.config("timestamp").asInstanceOf[Option[Date]]))
  }

  def parseFile(file: File, filters: List[Filter]): List[LogEntity] = {
    println("Parsing file: " + file)

    var logEntities: List[LogEntity] = List()
    val fileLines = Source.fromFile(file).getLines.toList
    val logFormat = identifyFormat(fileLines.head)
    var lines: List[String] = List()

    val filter = (lines: List[String]) => if (lines.nonEmpty) {
      val logEntity = new LogEntity(lines, logFormat)
      if (filters.dropWhile(_.filter(logEntity)).isEmpty) logEntities = logEntities :+ logEntity
    }

    val isBeginLine = (line: String) => Converter.convertDateToRegex(logFormat.config("timestamp")).r.findFirstIn(line).isDefined

    for (line <- fileLines) {
      if (isBeginLine(line)) {
        filter(lines)
        lines = List()
      }
      lines = lines :+ line
    }
    filter(lines)
    logEntities.foreach(_.setFileName(file.toString))
    logEntities
  }

  def mergeFilter(logEntities: List[LogEntity]): List[LogEntity] = {
    val filters = setMergeFilters()
    logEntities.filter(l => filters.dropWhile(_.filter(l)).isEmpty)
  }

  def generate(logEntities: List[LogEntity], path: String): Unit = {
    val writer = new PrintWriter(new File(path))
    logEntities.foreach(l => {
      val length = l.fileName.length
      var delimiter = ""
      for (_ <- 0 to length) yield delimiter += "="
      writer.write(delimiter + "\n")
      writer.write(l.fileName + ": \n")
      l.content.foreach(s => writer.write(s + "\n"))
    })
    writer.flush()
    println("Write " + logEntities.length + " records.")
    writer.close()
  }

  def getResult(logEntities: List[LogEntity]): List[String] = {
    var result: List[String] = List()
    logEntities.foreach(l => {
      result = result :+ l.fileName + ": "
      l.content.foreach(s => result = result :+ s)
    })
    result
  }

  private def setMergeFilters(): List[Filter] = List(
    new DuplicationFilter
  )

  private def setFileFilters(): List[Filter] = List(
    new TimeFilter("20000101 00:00:00,000", "20500101 00:00:00,000"),
    new LevelFilter(List("WARN", "ERROR"))
  )

  private def identifyFormat(head: String): LogFormat = FormatLoader.logFormats
    .map(f => Converter.convertDateToRegex(f._2.config("timestamp")).r.findFirstIn(head) match {
      case Some(_) => (f._2, new LogEntity(List(head), f._2).config.count(_._2.isDefined))
      case None => (f._2, -1)
    }).toList.sortBy(_._2)(Ordering[Int].reverse).head._1
}
