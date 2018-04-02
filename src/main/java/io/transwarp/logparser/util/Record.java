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
    private String duplicationIdentifier;

    public Record(List<String> record) {
        if (record.size() == 0) throw new IllegalArgumentException();
        content = new ArrayList<>();
        content.addAll(record);
        setIsException();
        setLevel();
        setDuplicationIdentifier();
    }

    private void setIsException() {
        isException = content.parallelStream().anyMatch(s -> s.startsWith("\tat "));
    }

    private void setLevel() {
        Pattern pattern = Pattern.compile("\\[\\s*(INFO|WARN|WARNING|ERROR|DEBUG)\\s*]");
        Matcher matcher = pattern.matcher(content.get(0));
        if (matcher.find())
            level = matcher.group(1);
        else
            level = null;
    }

    private void setDuplicationIdentifier() {
        for (String s : content) {
            if (s.startsWith("\tat ")) {
                duplicationIdentifier += s;
                return;
            }
            duplicationIdentifier = s;
        }
        duplicationIdentifier = null;
    }

    public List<String> getContent() {
        return content;
    }

    public boolean isException() {
        return isException;
    }

    public String getLevel() {
        return level;
    }

    public String getDuplicationIdentifier() {
        return duplicationIdentifier;
    }
}
