package org.n3r.acc.compare;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.n3r.acc.process.DataProcessor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AccDemo {
    public static void main(String[] args) throws IOException {
        Map<String, String> context = new HashMap<String, String>();
        for (int i = 0, ii = args.length; i < ii; ++i) {
            String arg = args[i];
            String key = StringUtils.substringBefore(arg, "=");
            String value = StringUtils.substringAfter(arg, "=");
            context.put(key ,value);
        }

        if (args.length == 0) {
            context.put("accountDay", "20130619");
            context.put("provinceCode", "17");
        }

        FileUtils.deleteDirectory(new File(".\\bdb\\accdemo"));

        DataProcessor dataProcessor = new DataProcessor().fromSpec("demoAcc", context);
        dataProcessor.process();

        DataCompare dc = new DataCompareBuilder().fromSpec("demoAcc", context).get();
        dc.compare();
    }
}
