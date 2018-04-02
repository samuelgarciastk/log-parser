package io.transwarp.logparser.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: stk
 * Date: 18/3/31
 */
public class DuplicationFilter implements Filter {
    private Set<String> existed = new HashSet<>();

    @Override
    public boolean filter(List<String> record) {
        String keyLine = null;
        for (String s : record) {
            if (s.startsWith("\tat ")) {
                keyLine += s;
                break;
            }
            keyLine = s;
        }
        if (keyLine == null) throw new NullPointerException();
        return existed.add(keyLine);
    }

    @Override
    public void clean() {
        existed.clear();
    }
}
