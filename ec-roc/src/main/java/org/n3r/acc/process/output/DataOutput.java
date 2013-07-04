package org.n3r.acc.process.output;


import java.util.Map;

public interface DataOutput {
    void setDataFieldNameIndex(Map<String, Integer> dataFieldNameIndex);

    void outputLine(Object[] fields);

    void finishOutput();

}
