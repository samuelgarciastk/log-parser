package io.transwarp.logparser.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class Record {
    private static final List<String> patterns = Loader.loadToList("pattern");
    private static final List<String> levels = Loader.loadToList("level");
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
        time = patterns.stream().map(s -> {
            Matcher matcher = Pattern.compile(Loader.convertDateToRegex(s)).matcher(content.get(0));
            if (matcher.find()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s.replace("T", "'T'"));
                try {
                    return simpleDateFormat.parse(matcher.group(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList()).get(0);
    }

    private void setLevel() {
        Pattern pattern = Pattern.compile("[^\\w]+(" + levels.stream().collect(Collectors.joining("|")) + ")[^\\w]+");
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
