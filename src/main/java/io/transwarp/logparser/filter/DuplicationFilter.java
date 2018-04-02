package io.transwarp.logparser.filter;

import io.transwarp.logparser.util.Record;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Author: stk
 * Date: 18/3/31
 */
public class DuplicationFilter implements Filter {
    private Set<String> existed = new HashSet<>();

    @Override
    public boolean filter(Record record) {
        return Optional.of(record).map(Record::getDuplicationIdentifier).map(s -> existed.add(s)).orElse(false);
    }
}
