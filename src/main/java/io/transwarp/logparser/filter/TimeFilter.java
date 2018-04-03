package io.transwarp.logparser.filter;

import io.transwarp.logparser.util.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: stk
 * Date: 2018/4/3
 */
public class TimeFilter implements Filter {
    private Date beginTime;
    private Date endTime;

    public TimeFilter(String beginTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss,SSS");
        try {
            this.beginTime = format.parse(beginTime);
            this.endTime = format.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean filter(Record record) {
        Date current = record.getTime();
        if (current == null) return false;
        return current.compareTo(beginTime) >= 0 && current.compareTo(endTime) <= 0;
    }
}
