package io.transwarp.logparser;

import io.transwarp.logparser.util.FileParser;
import io.transwarp.logparser.util.Record;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 18/3/27
 */
public class Parser {
    public static void main(String[] args) {
        String folderPath = "C:\\Users\\stk\\Downloads\\logs\\transwarp-manager";
        String logPath = "C:\\Users\\stk\\Downloads\\logs\\final.log";
        Parser parser = new Parser();
        List<Path> logs = parser.parseFolder(folderPath);
        List<Record> records = parser.merge(logs);
        parser.generate(records, logPath);
    }

    public List<Path> parseFolder(String folderPath) {
        try {
            return Files.walk(Paths.get(folderPath), 1)
                    .filter(path -> path.toString().endsWith(".log"))
                    .filter(this::isExceptionLog)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("Cannot resolve path: " + folderPath);
        throw new IllegalArgumentException();
    }

    public List<Record> merge(List<Path> paths) {
        FileParser fileParser = new FileParser();
//        return paths.parallelStream().map(fileParser::parseFile).reduce(this::mergeRecords).orElseThrow(NullPointerException::new);
        return paths.parallelStream().map(path -> {
            List<Record> records = fileParser.parseFile(path);
            System.err.println(path.getFileName() + ": " + records.size() + " records");
            return records;
        }).reduce(this::mergeRecords).orElseThrow(NullPointerException::new);
    }

    private List<Record> mergeRecords(List<Record> record1, List<Record> record2) {
        record1.addAll(record2);
        record1.sort((o1, o2) -> {
            Date date1 = o1.getTime();
            Date date2 = o2.getTime();
            return Integer.compare(date1.compareTo(date2), 0);
        });
        return record1;
    }

    private boolean isExceptionLog(Path path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("\tat ")) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void generate(List<Record> records, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            records.forEach(record -> record.getContent().forEach(s -> {
                try {
                    writer.write(s + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
            writer.flush();
            System.out.println("Write " + records.size() + " records.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
