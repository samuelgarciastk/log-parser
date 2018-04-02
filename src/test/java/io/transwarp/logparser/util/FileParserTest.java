package io.transwarp.logparser.util;

import io.transwarp.logparser.Parser;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

/**
 * Author: stk
 * Date: 2018/4/2
 */
public class FileParserTest {
    @Test
    public void parseFolder() {
        FileParser fileParser = new FileParser();
        List<Record> records = fileParser.parseFile(Paths.get("C:\\Users\\stk\\Downloads\\logs\\elasticsearch\\elasticsearch-2018-03-11.log"));
        Parser parser = new Parser();
        parser.generate(records, "C:\\Users\\stk\\Downloads\\logs\\test-result.log");
    }
}
