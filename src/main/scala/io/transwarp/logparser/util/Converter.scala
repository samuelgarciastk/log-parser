package io.transwarp.logparser.util

/**
  * Author: stk
  * Date: 2018/4/4
  */
object Converter {
  def convertDateToRegex(format: String): String = "^" +
    format.replace("yyyy", "\\d{4}")
      .replace("MM", "\\d{2}")
      .replace("dd", "\\d{2}")
      .replace("HH", "\\d{2}")
      .replace("mm", "\\d{2}")
      .replace("ss", "\\d{2}")
      .replace("SSS", "\\d{3}")
      .replace("[", "\\[")
      .replace("]", "\\]")

  def dateEscape(date: String): String = "(?:(?![aDdEFGHhKkMmSsWwyZz])[a-zA-Z])+".r.replaceAllIn(date, "'$0'")
}
