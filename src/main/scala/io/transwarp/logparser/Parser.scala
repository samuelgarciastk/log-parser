package io.transwarp.logparser

import java.io._
import java.nio.file.Paths
import java.util.zip.ZipInputStream

import io.transwarp.logparser.util.{FileParser, Record}

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/3
  */
object Parser {
  def main(args: Array[String]): Unit = {
    val zipPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\logs.zip"
    val folderPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\zip"
    val logPath = "C:\\Users\\stk\\Downloads\\test-data\\logs\\final.log"
    unzip(zipPath, folderPath)
    generate(FileParser.parseRecord(merge(getFiles(new File(folderPath)).toList)), logPath)
  }

  def getFiles(file: File): Array[File] = {
    val files = file.listFiles
    files.filter(_.isFile).filter(_.getName.endsWith(".log"))
      .filter(Source.fromFile(_).getLines.exists(_.startsWith("\tat "))) ++
      files.filter(_.isDirectory).flatMap(getFiles)
  }

  def merge(files: List[File]): List[Record] = {
    files.map(file => {
      val records = FileParser.parseFile(file)
      System.err.println(file.getName + ": " + records.length + " records.")
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

  def unzip(file: String, path: String): Unit = {
    val folder = new File(path)
    if (!folder.exists) folder.mkdir
    val in = new ZipInputStream(new FileInputStream(file))
    Stream.continually(in.getNextEntry).takeWhile(_ != null).foreach(file => {
      if (!file.isDirectory) {
        val outPath = Paths.get(path).resolve(file.getName)
        val parent = outPath.getParent
        if (!parent.toFile.exists) parent.toFile.mkdirs
        val out = new FileOutputStream(outPath.toFile)
        val buffer = new Array[Byte](4096)
        Stream.continually(in.read(buffer)).takeWhile(_ != -1).foreach(out.write(buffer, 0, _))
      }
    })
  }
}
