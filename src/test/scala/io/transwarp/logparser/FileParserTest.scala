package io.transwarp.logparser

import java.io.File

import io.transwarp.logparser.filter.{LevelFilter, TimeFilter}
import io.transwarp.logparser.util.LogWriter
import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/11
  */
class FileParserTest {
  @Test
  def parseFile(): Unit = {
    val file = "C:\\Users\\stk\\Downloads\\test-data\\logs\\elasticsearch\\elasticsearch-2018-03-19.log"
    val path = "C:\\Users\\stk\\Downloads\\test-data\\logs\\test-result.log"
    LogWriter.writeLogEntries(FileParser.parseFile(new File(file), List(
      new TimeFilter(List("20000101 00:00:00,000", "20500101 00:00:00,000")),
      new LevelFilter(List("WARN", "ERROR"))
    )), path)
  }

  @Test
  def identifyFormat(): Unit = {
    val s1 = "[2018-03-21T17:50:37,137][DEBUG][o.e.a.s.TransportSearchAction] [4BWodNv] All shards failed for phase: [query]"
    val s2 = "2018-03-22 23:25:10,895  INFO io.transwarp.manager.agent.actor.AgentDispatcher:  [agent-akka.actor.default-dispatcher-2] (AgentDispatcher.java:258) - Sending Heartbeat to akka.tcp://manager@172.16.3.7:2551/user/masterDispatcher"
    val s3 = "2018-03-25 15:00:52,926 INFO  txn.TxnHandler: (TxnHandler.java:checkRetryable(1817)) [Metastore-compact-cleaner-1000004()] - Non-retryable error: Unknown column 'cq_highest_txn_id' in 'field list'(SQLState=42S22,ErrorCode=1054)"
    println(FileParser.identifyFormat(s1))
    println(FileParser.identifyFormat(s2))
    println(FileParser.identifyFormat(s3))
  }
}
