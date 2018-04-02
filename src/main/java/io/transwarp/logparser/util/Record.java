package io.transwarp.logparser.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class Record {
    private List<String> content;
    private boolean isException;
    private String level;

    public Record(List<String> record) {
        content = new ArrayList<>();
        content.addAll(record);
        setIsException();
    }

    private void setIsException() {
        isException = content.parallelStream().anyMatch(s -> s.startsWith("\tat "));
    }

    private void setLevel() {
        Pattern pattern = Pattern.compile("\\s+(INFO|WARN|WARNING|ERROR|DEBUG)\\s+");
        Matcher matcher = pattern.matcher(content.get(0));
        if (matcher.find())
            level = matcher.group(1);
        else
            level = null;
    }

    public List<String> getContent() {
        return content;
    }

    public boolean isException() {
        return isException;
    }
}
