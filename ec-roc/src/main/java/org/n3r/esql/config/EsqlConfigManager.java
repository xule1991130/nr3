package org.n3r.esql.config;

import static com.google.common.collect.Maps.*;

import java.util.Map;

import org.n3r.config.Config;
import org.n3r.config.Configable;
import org.n3r.esql.ex.EsqlConfigException;

public class EsqlConfigManager {
    private static Map<String, EsqlConfigable> configMap = newHashMap();

    static {
        initialize();
    }

    private static void initialize() {
        Configable connConfig = Config.subset("connectionName");

        for (String connectionName : connConfig.getKeyPrefixes()) {
            Configable config = connConfig.subset(connectionName);
            EsqlConfigable EsqlConfigable = config.exists("jndiName")
                    ? createDsConfig(config) : createSimpleConfig(config);

                    configMap.put(connectionName, EsqlConfigable);
        }
    }

    private static EsqlConfigable createDsConfig(Configable connConfig) {
        EsqlDsConfig dsConfig = new EsqlDsConfig();
        dsConfig.setJndiName(connConfig.getStr("jndiName"));
        dsConfig.setInitial(connConfig.getStr("java.naming.factory.initial", ""));
        dsConfig.setUrl(connConfig.getStr("java.naming.provider.url", ""));
        dsConfig.setTransactionType(connConfig.getStr("transactionType"));

        return dsConfig;
    }

    private static EsqlConfigable createSimpleConfig(Configable connConfig) {
        EsqlSimpleConfig simpleConfig = new EsqlSimpleConfig();
        simpleConfig.setDriver(connConfig.getStr("driver"));
        simpleConfig.setUrl(connConfig.getStr("url"));
        simpleConfig.setUser(connConfig.getStr("user"));
        simpleConfig.setPass(connConfig.getStr("password"));
        simpleConfig.setTransactionType(connConfig.getStr("transactionType"));

        return simpleConfig;
    }

    public static EsqlConfigable getConfig(String connectionName) {
        EsqlConfigable esqlConfigable = configMap.get(connectionName);
        if (esqlConfigable != null) return esqlConfigable;

        throw new EsqlConfigException(
                "esql connection name " + connectionName + " is not properly configed.");
    }
}
