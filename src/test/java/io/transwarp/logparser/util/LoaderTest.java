package io.transwarp.logparser.util;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Author: stk
 * Date: 18/4/3
 */
public class LoaderTest {
    @Test
    public void convertDateToRegex() {
        try {
            Method method = Loader.class.getDeclaredMethod("convertDateToRegex", String.class);
            method.setAccessible(true);
            assert method.invoke(Loader.class, "[yyyy-MM-ddTHH:mm:ss,SSS]").equals("^\\[\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2},\\d{3}\\]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
