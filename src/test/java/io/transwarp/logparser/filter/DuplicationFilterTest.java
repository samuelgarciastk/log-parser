package io.transwarp.logparser.filter;

import io.transwarp.logparser.util.Record;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Author: stk
 * Date: 18/4/1
 */
public class DuplicationFilterTest {
    private List<String> testData1;
    private List<String> testData2;
    private List<String> testData3;

    @Before
    public void prepareData() {
        testData1 = List.of("[2018-02-22T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
                "java.lang.NullPointerException: null",
                "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
                "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
                "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]");
        testData2 = List.of("[2018-02-23T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
                "java.lang.NullPointerException: null",
                "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
                "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
                "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]");
        testData3 = List.of("[2018-02-23T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}");
    }

    @Test
    public void filter() {
        Filter filter = new DuplicationFilter();
        assert filter.filter(new Record(testData1));
        assert !filter.filter(new Record(testData2));
        assert !filter.filter(new Record(testData3));
    }
}
