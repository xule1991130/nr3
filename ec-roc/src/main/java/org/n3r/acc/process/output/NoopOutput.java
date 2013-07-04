package org.n3r.acc.process.output;

import org.n3r.config.Configable;
import org.n3r.core.tag.EcRocTag;

import java.util.Map;

@EcRocTag("Noop")
public class NoopOutput implements DataOutput {
    @Override
    public void setDataFieldNameIndex(Map<String, Integer> dataFieldNameIndex) {

    }

    @Override
    public void outputLine(Object[] fields) {

    }


    @Override
    public void finishOutput() {

    }
}
