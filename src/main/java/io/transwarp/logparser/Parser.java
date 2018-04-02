package io.transwarp.logparser;

import io.transwarp.logparser.util.FileParser;
import io.transwarp.logparser.util.Record;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 18/3/27
 */
public class Parser {
    public static void main(String[] args) {
        String folderPath = "C:\\Users\\stk\\Downloads\\logs\\elasticsearch";
        String logPath = "final.log";
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
        fileParser.initFilters();
        return paths.stream().map(path -> {
            fileParser.clean();
            return fileParser.parseFile(path);
        }).reduce(this::mergeRecords).get();
    }

    private List<Record> mergeRecords(List<Record> record1, List<Record> record2) {
        return null;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
