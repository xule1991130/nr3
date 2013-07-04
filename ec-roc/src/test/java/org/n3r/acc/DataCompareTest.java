package org.n3r.acc;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.n3r.acc.compare.DataCompare;
import org.n3r.acc.compare.DataCompareBuilder;
import org.n3r.acc.process.DataProcessor;

import java.io.File;

public class DataCompareTest {
    @Test
    public void test1() throws Exception {
        FileUtils.deleteDirectory(new File("C:\\bdb\\bdbdemo3"));
        DataProcessor dataProcessor = new DataProcessor().fromSpec("broadbandAcc", null);
        dataProcessor.process();

DataCompare dc = new DataCompareBuilder().fromSpec("broadband", null).get();
dc.compare();
}


        }
