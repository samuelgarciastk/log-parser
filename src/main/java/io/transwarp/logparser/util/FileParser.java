package io.transwarp.logparser.util;

import io.transwarp.logparser.filter.DuplicationFilter;
import io.transwarp.logparser.filter.Filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class FileParser {
    private static final List<String> patterns = PatternLoader.loadPattern();
    private List<Filter> filters;

    private List<String> parseFile(String path) {
        System.out.println("Parsing file: " + path);
        List<String> log = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            List<String> record = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (isBegin(line)) {
                    if (filter(record))
//                        record
                        record.forEach(i -> {

                        });
                    record.clear();
                }
                record.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initFilters() {
        filters = new ArrayList<>();
        filters.add(new DuplicationFilter());
    }

    private boolean isBegin(String line) {
        return patterns.parallelStream().anyMatch(i -> Pattern.compile(i).matcher(line).find());
    }

    private boolean filter(List<String> record) {
        return filters.stream().dropWhile(i -> i.filter(record)).count() == 0;
    }
}
