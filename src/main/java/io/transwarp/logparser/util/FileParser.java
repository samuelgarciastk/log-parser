package io.transwarp.logparser.util;

import io.transwarp.logparser.filter.DuplicationFilter;
import io.transwarp.logparser.filter.ExceptionFilter;
import io.transwarp.logparser.filter.Filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
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

    public List<Record> parseFile(Path path) {
        System.out.println("Parsing file: " + path);
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path.toString()))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (isBegin(line)) {
                    lines.add(line);
                    Record record = new Record(lines);
                    if (filter(record)) records.add(record);
                    lines.clear();
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public void initFilters() {
        filters = new ArrayList<>();
        filters.add(new ExceptionFilter());
        filters.add(new DuplicationFilter());
    }

    public void clean() {
        filters.forEach(Filter::clean);
    }

    private boolean isBegin(String line) {
        return patterns.parallelStream().anyMatch(i -> Pattern.compile(i).matcher(line).find());
    }

    private boolean filter(Record record) {
        return filters.stream().dropWhile(i -> i.filter(record)).count() == 0;
    }
}
