package io.transwarp.logparser.filter;

import io.transwarp.logparser.util.Record;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class ExceptionFilter implements Filter {
    @Override
    public boolean filter(Record record) {
        return record.isException();
    }

    @Override
    public void clean() {
    }
}
