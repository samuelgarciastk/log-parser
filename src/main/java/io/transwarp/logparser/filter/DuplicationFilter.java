package io.transwarp.logparser.filter;

import io.transwarp.logparser.util.Record;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: stk
 * Date: 18/3/31
 */
public class DuplicationFilter implements Filter {
    private Set<String> existed = new HashSet<>();

    @Override
    public boolean filter(Record record) {
        if (record.getDuplicationIdentifier() == null) return false;
        return existed.add(record.getDuplicationIdentifier());
    }

    @Override
    public void clean() {
        existed.clear();
    }
}
