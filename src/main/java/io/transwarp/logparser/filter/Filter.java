package io.transwarp.logparser.filter;

import io.transwarp.logparser.util.Record;

/**
 * Author: stk
 * Date: 18/3/27
 */
public interface Filter {
    /**
     * Filter the record.
     *
     * @param record one record
     * @return if the record needs to be save into file, then return true.
     */
    boolean filter(Record record);

    /**
     * Clean status.
     */
    void clean();
}
