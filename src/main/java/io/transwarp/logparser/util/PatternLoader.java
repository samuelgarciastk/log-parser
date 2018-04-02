package io.transwarp.logparser.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: stk
 * Date: 18/3/31
 */
public class PatternLoader {
    public static List<String> loadPattern() {
        List<String> result = new ArrayList<>();
        try (InputStreamReader in = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("pattern"), "UTF-8");
             BufferedReader br = new BufferedReader(in)) {
            String line;
            while ((line = br.readLine()) != null) result.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
