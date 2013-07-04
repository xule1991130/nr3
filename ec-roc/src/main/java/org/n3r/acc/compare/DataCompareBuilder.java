package org.n3r.acc.compare;

import org.n3r.acc.utils.AccUtils;
import org.n3r.config.Config;
import org.n3r.config.Configable;
import org.n3r.core.tag.EcTagUtils;
import org.n3r.core.tag.FromSpecConfig;
import org.n3r.core.tag.Spec;
import org.n3r.core.tag.SpecParser;

import java.util.Map;

public class DataCompareBuilder {
    private DataCompare dataCompare;

    public DataCompareBuilder fromSpec(String specName, Map<String, String> context) {
        Configable config = Config.subset("DataCompare." + specName);
        String compareImpl = config.getStr("compareImpl", "@Outside");


        dataCompare = AccUtils.parseSpec(compareImpl, DataCompare.class, config, context) ;

        return this;
    }

    public DataCompare get() {
        return dataCompare;
    }
}
