package io.transwarp.logparser.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class Record {
    private static final List<String> patterns = Loader.loadList("pattern");
    private static final List<String> levels = Loader.loadList("level");
    private List<String> content;
    private boolean isException;
    private Date time;
    private String level;
    private String duplicationIdentifier;

    public Record(List<String> record) {
        if (record == null || record.size() == 0) throw new IllegalArgumentException("Invalid record.");
        content = new ArrayList<>();
        content.addAll(record);
        setIsException();
        setTime();
        setLevel();
        setDuplicationIdentifier();
    }

    private void setIsException() {
        isException = content.parallelStream().anyMatch(s -> s.startsWith("\tat "));
    }

    private void setTime() {
    }

    private void setLevel() {
        Pattern pattern = Pattern.compile("\\[\\s*(" + levels.stream().collect(Collectors.joining("|")) + ")\\s*]");
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

    public Date getTime() {
        return time;
    }

    public String getLevel() {
        return level;
    }

    public String getDuplicationIdentifier() {
        return duplicationIdentifier;
    }
}
