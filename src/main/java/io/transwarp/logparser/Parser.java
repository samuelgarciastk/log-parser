package io.transwarp.logparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        return null;
    }

    public void merge(List<Path> paths) {

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
}
