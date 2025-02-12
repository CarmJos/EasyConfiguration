package cc.carm.lib.configuration;

import cc.carm.lib.configuration.sql.SQLConfigProvider;
import cc.carm.lib.easysql.api.SQLManager;
import org.jetbrains.annotations.NotNull;

public class EasyConfiguration {

    private EasyConfiguration() {
    }

    public static SQLConfigProvider from(@NotNull SQLManager sqlManager, @NotNull String tableName, @NotNull String namespace) {
        SQLConfigProvider provider = new SQLConfigProvider(sqlManager, tableName, namespace);
        try {
            provider.initializeConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static SQLConfigProvider from(@NotNull SQLManager sqlManager, @NotNull String tableName) {
        return from(sqlManager, tableName, "base");
    }

}
