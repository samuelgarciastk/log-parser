package io.transwarp.logparser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 18/4/1
 */
public class DuplicationFilterTest {
    private List<String> testData;

    @Before
    public void prepareData() {
        testData = List.of("[2018-03-01T16:32:09,431][WARN ][o.e.b.ElasticsearchUncaughtExceptionHandler] [] uncaught exception in thread [main]",
                "[2018-03-01T16:32:09,423][ERROR][o.e.b.Bootstrap          ] Exception",
                "[2018-04-01T16:32:09,431][WARN ][o.e.b.ElasticsearchUncaughtExceptionHandler] [] uncaught exception in thread [main]",
                "[2018-05-01T16:32:09,431][WARN ][o.e.b.ElasticsearchUncaughtExceptionHandler] [] uncaught exception in thread [main]",
                "[2018-06-01T16:32:09,423][ERROR][o.e.b.Bootstrap          ] Exception");
    }

    @Test
    public void filter() {
        Filter filter = new DuplicationFilter();
        List<Boolean> result = testData.stream().map(i -> filter.filter(List.of(i))).collect(Collectors.toList());
        result.forEach(System.out::println);
    }
}
