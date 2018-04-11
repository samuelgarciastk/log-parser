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
}
