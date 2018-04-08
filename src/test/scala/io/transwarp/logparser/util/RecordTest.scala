package io.transwarp.logparser.util

import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/4
  */
class RecordTest {
  val list = List("[2018-02-22Q20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
    "java.lang.NullPointerException: null",
    "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
    "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
    "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]")
  val list2 = List("[2018-03-05T11:23:47,558][WARN ][o.e.n.Node               ] version [5.4.1-SNAPSHOT] is a pre-release version of Elasticsearch and is not suitable for production")

  @Test
  def record(): Unit = {
    val record = new Record(list)
    assert(record.isException)
    println(record.level)
    println(record.duplicationIdentifier)
    println(record.time)
    val record2 = new Record(list2)
    println(record2.duplicationIdentifier)
  }
}
