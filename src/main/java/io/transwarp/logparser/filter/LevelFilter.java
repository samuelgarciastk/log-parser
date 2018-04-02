package io.transwarp.logparser.filter;

import io.transwarp.logparser.util.Record;

import java.util.Optional;

/**
 * Author: stk
 * Date: 18/4/2
 */
public class LevelFilter implements Filter {
    @Override
    public boolean filter(Record record) {
        return Optional.of(record).map(Record::getLevel).filter(s -> s.equals("WARN") || s.equals("ERROR")).stream().count() == 1;
    }
}
