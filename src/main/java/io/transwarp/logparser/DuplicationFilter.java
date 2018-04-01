package io.transwarp.logparser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 18/3/31
 */
public class DuplicationFilter implements Filter {
    private final List<String> patterns = PatternParser.getPatterns();
    private Set<String> existed = new HashSet<>();

    @Override
    public boolean filter(List<String> record) {
        String keyLine = record.get(0);
        List<String> results = patterns.parallelStream().map(i -> {
            Matcher matcher = Pattern.compile(i).matcher(keyLine);
            if (matcher.find()) return matcher.replaceFirst("");
            return null;
        }).collect(Collectors.toList());
        return existed.add(results.get(0));
    }
}
