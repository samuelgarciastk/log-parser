package io.transwarp.logparser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: stk
 * Date: 18/3/27
 */
public class Parser {
    private final List<String> patterns = PatternParser.getPatterns();
    private List<Filter> filters;
    private List<Filter> subFilters;

    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.initFilters();
        parser.parseFolder("C:\\Users\\stk\\Downloads\\logs\\", "C:\\Users\\stk\\Downloads\\logs\\parsed\\");
    }

    private void initFilters() {
        filters = new ArrayList<>();
        filters.add(new ExceptionFilter());
        subFilters = new ArrayList<>();
        subFilters.add(new DuplicationFilter());
    }

    private void parseFolder(String inPath, String outPath) {
        Pattern pattern = Pattern.compile("^(.*)\\.log");
        try {
            Files.walk(Paths.get(inPath), 1).filter(Files::isRegularFile).forEach(i -> {
                Matcher matcher = pattern.matcher(i.getFileName().toString());
                if (matcher.find())
                    parseFile(i.toString(), outPath + matcher.replaceFirst("$1-parsed.log"));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseFile(String inPath, String outPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(inPath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {
            List<String> record = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (isBegin(line)) {
                    if (filter(record))
                        record.forEach(i -> {
                            try {
                                bw.write(i + System.getProperty("line.separator"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    record.clear();
                }
                record.add(line);
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isBegin(String line) {
        return patterns.parallelStream().anyMatch(i -> Pattern.compile(i).matcher(line).find());
    }

    private boolean filter(List<String> record) {
        if (filters.parallelStream().noneMatch(i -> i.filter(record))) return false;
        return subFilters.parallelStream().anyMatch(i -> i.filter(record));
    }
}
