package io.transwarp.logparser.util

import java.io._
import java.nio.file.Path
import java.util.zip.ZipInputStream

import scala.io.Source

/**
  * Author: stk
  * Date: 2018/4/10
  */
object ToolUtils {
  def unzip(file: String, path: Path): Unit = {
    val in = new ZipInputStream(new FileInputStream(file))
    Stream.continually(in.getNextEntry).takeWhile(_ != null).foreach(f => {
      if (!f.isDirectory) {
        val outPath = path.resolve(f.getName)
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
    files.filter(_.isFile) ++ files.filter(_.isDirectory).flatMap(getAllFiles)
  }

  def fileToString(path: String): String = {
    val file = Source.fromInputStream(this.getClass.getClassLoader.getResourceAsStream(path))
    val result = file.getLines.mkString("\n")
    file.close()
    result
  }

  def calculateTime[A](function: => A): (A, Double) = {
    val start = System.nanoTime
    val result = function
    val end = System.nanoTime
    (result, (end - start) / 1000000000d)
  }
}
