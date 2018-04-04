package io.transwarp.logparser

import io.transwarp.logparser.filter.{DuplicationFilter, ExceptionFilter, LevelFilter, TimeFilter}
import io.transwarp.logparser.util.Record
import org.junit.Test

/**
  * Author: stk
  * Date: 2018/4/4
  */
class FilterTest {
  var data1 = List("[2018-02-22T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
    "java.lang.NullPointerException: null",
    "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
    "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
    "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]")
  var data2 = List("[2018-02-23T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
    "java.lang.NullPointerException: null",
    "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
    "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
    "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]")
  var data3 = List("[2018-02-23T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}")
  var data4 = List("[2018-02-23T20:34:41,062][INFO ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}")

  @Test
  def exceptionFilter(): Unit = {
    val filter = new ExceptionFilter
    assert(filter.filter(new Record(data1)))
    assert(!filter.filter(new Record(data3)))
  }

  @Test
  def timeFilter(): Unit = {
    val filter = new TimeFilter("20180101 00:00:00,000", "20180301 00:00:00,000")
    assert(filter.filter(new Record(data1)))
  }

  @Test
  def levelFilter(): Unit = {
    val filter = new LevelFilter
    assert(filter.filter(new Record(data1)))
    assert(!filter.filter(new Record(data4)))
  }

  @Test
  def duplicationFilter(): Unit = {
    val filter = new DuplicationFilter
    assert(filter.filter(new Record(data1)))
    assert(!filter.filter(new Record(data2)))
    assert(!filter.filter(new Record(data3)))
  }
}
