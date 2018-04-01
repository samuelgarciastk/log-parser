package io.transwarp.logparser;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Author: stk
 * Date: 18/3/31
 */
public class ExceptionFilter implements Filter {
    @Override
    public boolean filter(List<String> record) {
        return record.parallelStream().anyMatch(i -> Pattern.compile("^\tat ").matcher(i).find());
    }
}
