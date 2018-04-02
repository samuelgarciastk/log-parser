package io.transwarp.logparser.util;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class RecordTest {
    private List<String> testData;

    @Before
    public void prepareData() {
        testData = List.of("[2018-02-22T20:34:41,062][WARN ][r.suppressed             ] path: /es_testdata/doc/, params: {index=es_testdata, type=doc}",
                "java.lang.NullPointerException: null",
                "\tat java.util.Objects.requireNonNull(Objects.java:203) ~[?:1.8.0_25]",
                "\tat org.elasticsearch.action.index.IndexRequest.source(IndexRequest.java:464) ~[elasticsearch-5.4.3.jar:5.4.3]",
                "\tat org.elasticsearch.rest.action.document.RestIndexAction.prepareRequest(RestIndexAction.java:75) ~[elasticsearch-5.4.3.jar:5.4.3]");
    }

    @Test
    public void record() {
        Record record = new Record(testData);
        assert record.isException();
        assert record.getLevel().equals("WARN");
        System.out.println(record.getDuplicationIdentifier());
    }
}
