package org.n3r.acc.compare.impl;

import org.n3r.acc.compare.BatchNoCreator;
import org.n3r.core.tag.EcRocTag;

@EcRocTag("currentTimeMillis")
public class CurrentTimeMillisBatchNoCreator implements BatchNoCreator{
    @Override
    public String createBatchNo() {
        return "" + System.currentTimeMillis();
    }
}
