package io.transwarp.logparser;

import java.util.List;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class Test {
    @org.junit.Test
    public void test() {
        List<Boolean> testValue = List.of(true, true, true, true, false, true);
        System.out.println(testValue.stream().dropWhile(Boolean::booleanValue).count());
    }
}
