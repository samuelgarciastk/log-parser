package io.transwarp.logparser.util

import io.transwarp.logparser.FileParser
import org.junit.Test

/**
  * Author: stk
  * Date: 18/4/8
  */
class LogEntryTest {
  val list1 = List("[2018-02-22T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
    "java.lang.NullPointerException: null",
    "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
    "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
    "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]")
  val list2 = List("[2018-03-05T11:23:47,558][WARN ][o.e.n.Node               ] version [5.4.1-SNAPSHOT] is a pre-release version of Elasticsearch and is not suitable for production")
  val list3 = List("2018-03-15 00:54:10,899  INFO io.transwarp.manager.agent.actor.AgentDispatcher:  [agent-akka.actor.default-dispatcher-17] (AgentDispatcher.java:258) - Sending Heartbeat to akka.tcp://manager@172.16.3.7:2551/user/masterDispatcher")
  val list4 = List("2018-04-10 08:40:40,612 INFO  ql.Driver: (Driver.java:runInternal(1658)) [HiveServer2-Handler-Pool: Thread-1732(SessionHandle=1fcdf292-4179-4f17-9772-2e468c333915)] - Driver.run finished at 1523364040612, duration 19ms, query hive_20180410084040_0383ef03-890a-484a-b33e-0d247432adb0")

  @Test
  def logEntry(): Unit = {
    val logEntry = new LogEntry(list4, FileParser.identifyFormat(list4.head))
    logEntry.format.foreach(println)
  }
}
