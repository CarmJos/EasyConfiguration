package config;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.beecp.BeeDataSourceConfig;

public class SQLConfigTest {


    public void test() {
        BeeDataSourceConfig config = new BeeDataSourceConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:file:target/test;DB_CLOSE_DELAY=-1;MODE=MySQL;");
        SQLManager manager = EasySQL.createManager(config);
        manager.setDebugMode(true);

//        SQLConfigProvider provider = EasyConfiguration.from(manager, "conf_test", "TESTING");
//
//        ConfigurationTest.testDemo(provider);
//        ConfigurationTest.testInner(provider);
//
//        ConfigurationTest.save(provider);
//
//        EasySQL.shutdownManager(manager);
    }

}
