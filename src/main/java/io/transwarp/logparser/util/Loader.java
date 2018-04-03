package io.transwarp.logparser.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 18/3/31
 */
public class Loader {
    /**
     * Get configuration file from resource directory or the path where the artifact is.
     *
     * @param name file name
     * @return InputStreamReader
     */
    private static InputStreamReader getConf(String name) {
        File conf = new File(System.getProperty("user.dir") + File.separator + name);
        try {
            return new InputStreamReader(new FileInputStream(conf), "UTF-8");
        } catch (FileNotFoundException e) {
            try {
                return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.err.println("File not found: " + name);
        throw new IllegalArgumentException();
    }

    public static List<String> loadToList(String name) {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(getConf(name))) {
            String line;
            while ((line = reader.readLine()) != null) result.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> loadDatePattern() {
        List<String> date = loadToList("pattern");
        return date.stream().map(Loader::convertDateToRegex).collect(Collectors.toList());
    }

    public static String convertDateToRegex(String format) {
        return "^" + format.replace("yyyy", "\\d{4}")
                .replace("MM", "\\d{2}")
                .replace("dd", "\\d{2}")
                .replace("HH", "\\d{2}")
                .replace("mm", "\\d{2}")
                .replace("ss", "\\d{2}")
                .replace("SSS", "\\d{3}")
                .replace("[", "\\[")
                .replace("]", "\\]");
    }
}
