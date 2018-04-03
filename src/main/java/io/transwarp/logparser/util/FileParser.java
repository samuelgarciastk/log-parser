package io.transwarp.logparser.util;

import io.transwarp.logparser.filter.*;

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
    private static final List<String> patterns = Loader.loadDatePattern();
    private List<Filter> filters;

    public List<Record> parseFile(Path path) {
        initFilters();
        System.out.println("Parsing file: " + path);
        List<Record> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) if (isBegin(line)) break; // Begin from first record.
            List<String> lines = new ArrayList<>();
            lines.add(line);

            while ((line = reader.readLine()) != null) {
                if (isBegin(line)) {
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

    private void initFilters() {
        filters = new ArrayList<>();
        filters.add(new TimeFilter("20000101 00:00:00,000", "20500101 00:00:00,000"));
        filters.add(new LevelFilter());
//        filters.add(new ExceptionFilter());
//        filters.add(new DuplicationFilter());
    }

    private boolean isBegin(String line) {
        return patterns.parallelStream().anyMatch(s -> Pattern.compile(s).matcher(line).find());
    }

    private boolean filter(Record record) {
        return filters.stream().dropWhile(filter -> filter.filter(record)).count() == 0;
    }
}
