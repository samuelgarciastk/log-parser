package io.transwarp.logparser

import io.transwarp.logparser.conf.FormatLoader
import io.transwarp.logparser.filter.{DuplicationFilter, ExceptionFilter, LevelFilter, TimeFilter}
import io.transwarp.logparser.util.LogEntry
import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/4
  */
class FilterTest {
  val data1 = List("[2018-02-22T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
    "java.lang.NullPointerException: null",
    "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
    "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
    "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]")
  val data2 = List("[2018-02-23T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
    "java.lang.NullPointerException: null",
    "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
    "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
    "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]")
  val data3 = List("[2018-02-23T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}")
  val data4 = List("[2018-02-23T20:34:41,062][INFO ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}")
  val data5 = List("2018-03-30 21:31:50,897  INFO io.transwarp.manager.agent.actor.AgentDispatcher:  [agent-alert-future-dispatcher-5958748] (AgentDispatcher.java:248) - Sending AlertReport to akka.tcp://manager@172.16.3.7:2551/user/masterDispatcher/alertGatewayActor")

  @Test
  def exceptionFilter(): Unit = {
    val filter = new ExceptionFilter(Nil)
    assert(filter.filter(new LogEntry(data1, FormatLoader.logFormats("es"))))
    assert(!filter.filter(new LogEntry(data3, FormatLoader.logFormats("es"))))
  }

  @Test
  def timeFilter(): Unit = {
    val filter = new TimeFilter(List("20000101 00:00:00,000", "20500101 00:00:00,000"))
    assert(filter.filter(new LogEntry(data5, FormatLoader.logFormats("manager"))))
  }

  @Test
  def levelFilter(): Unit = {
    val filter = new LevelFilter(List("WARN", "ERROR"))
    assert(filter.filter(new LogEntry(data1, FormatLoader.logFormats("es"))))
    assert(!filter.filter(new LogEntry(data4, FormatLoader.logFormats("es"))))
  }

  @Test
  def duplicationFilter(): Unit = {
    val filter = new DuplicationFilter(Nil)
    assert(filter.filter(new LogEntry(data1, FormatLoader.logFormats("es"))))
    assert(!filter.filter(new LogEntry(data2, FormatLoader.logFormats("es"))))
    assert(filter.filter(new LogEntry(data3, FormatLoader.logFormats("es"))))
  }
}
